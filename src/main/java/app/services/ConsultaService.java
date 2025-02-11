package app.services;

import app.dtos.PageDTO;
import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.mappers.ConsultaMapper;
import app.models.Consulta;
import app.models.MotivoCancelamento;
import app.repositories.ConsultaRepository;
import app.models.Medico;
import app.shared.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;
    private final MedicoService medicoService;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoService medicoService,
                           ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.medicoService = medicoService;
        this.consultaMapper = consultaMapper;
    }

    @Transactional
    public ConsultaResponseDTO criarConsulta(ConsultaRequestCreateDTO request) {
        var medico = request.idMedico() == null
                ? buscarMedicoDisponivel(request.dataConsulta())
                : medicoService.obterMedicoPorId(request.idMedico());

        validarRegrasInclusaoConsulta(Objects.requireNonNull(medico).getId(),
                request.idPaciente(), request.dataConsulta());

        Consulta consulta = consultaMapper.toEntity(request);
        consulta.setMedico(medico);// insere medico
        var consultaCriada = consultaRepository.save(consulta);
        return consultaMapper.toResponseDTO(consultaCriada);
    }

    @Transactional
    public ConsultaResponseDTO atualizarConsulta(Long id, ConsultaRequestUpdateDTO request) {
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada."));
        var dataConsulta = request.dataConsulta() != null
                ? request.dataConsulta() : consulta.getDataConsulta();
        var idMedico = request.idMedico() != null
                ? request.idMedico() : consulta.getMedico().getId();
        validarRegrasAlteracaoConsulta(idMedico, dataConsulta);
        consulta.setDataConsulta(dataConsulta);
        consulta.setMedico(medicoService.obterMedicoPorId(idMedico));
        var consultaAlterada = consultaRepository.save(consulta);
        return consultaMapper.toResponseDTO(consultaAlterada);
    }

    @Transactional
    public void cancelarConsulta(Long id, MotivoCancelamento request) {
        if (id == null) throw new IllegalArgumentException("O ID da consulta não pode ser nulo.");
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada."));
        consulta.setMotivoCancelamento(request);
        consulta.setStatus(false);
        consultaRepository.save(consulta);
    }

    public PageDTO<ConsultaResponseDTO> listarConsultas(Pageable paginacao) {
        Page<Consulta> consultas = consultaRepository.findAll(paginacao);
        Page<ConsultaResponseDTO> consultasPageDTO =
                consultas.map(consultaMapper::toResponseDTO );
        return new PageDTO<>(consultasPageDTO);
    }

    public ConsultaResponseDTO obterConsultaPorId(Long id) {
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado"));
        return consultaMapper.toResponseDTO(consulta);
    }

    private Medico buscarMedicoDisponivel(LocalDateTime data) {
        var medicos = medicoService.obterListaMedicosAtivos();
        for (Medico medico : medicos) {
            boolean consultaExistente = consultaRepository
                    .findConsultaByMedicoAndDataHora(medico.getId(), data).isPresent();
            if (!consultaExistente) {
                return medico;
            }
        }
        return null;
    }

    private void validarRegrasInclusaoConsulta(Long idMedico, Long idPaciente, LocalDateTime data) {
        validarHorarioFuncionamento(data);
        validarAntecedenciaMinima(data);
        validarDisponibilidadeMedico(idMedico, data);
        validarDisponibilidadePaciente(idPaciente, data);
    }

    private void validarRegrasAlteracaoConsulta(Long idMedico, LocalDateTime dataConsulta) {
        if (!medicoService.obterMedicoPorId(idMedico).getStatus()) {
            throw new MedicoInativeException("O médico está inativo.");
        }
        validarDisponibilidadeMedico(idMedico, dataConsulta);
        validarHorarioFuncionamento(dataConsulta);
        validarAntecedenciaMinima(dataConsulta);
    }

    // Regra 1: Valida horário de funcionamento (segunda a sábado, 08:00 às 18:00)
    private void validarHorarioFuncionamento(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime horaConsulta = data.toLocalTime();
        if (diaDaSemana == DayOfWeek.SUNDAY ||
                horaConsulta.isBefore(LocalTime.of(8, 0)) ||
                horaConsulta.isAfter(LocalTime.of(18, 0))) {
            throw new ConsultaValidationException
                    ("A Consulta deve ser agendada entre segunda e sábado, das 08:00 às 18:00.");
        }
    }

    // Regra 2: Valida a antecedência mínima de 30 minutos
    private void validarAntecedenciaMinima(LocalDateTime data) {
        LocalDateTime agora = LocalDateTime.now();
        Duration diferencaTempo = Duration.between(agora, data);
        if (diferencaTempo.toMinutes() < 30) throw new ConsultaValidationException
                ("A Consulta deve ser agendada com, no mínimo, 30 minutos de antecedência.");
    }

    // Regra 3: Valida se o paciente já possui um Consulta no mesmo dia;
    public void validarDisponibilidadePaciente(Long idPaciente, LocalDateTime data) {
        var consulta = consultaRepository
                .findConsultaByPacienteAndData(idPaciente, data);
        if(consulta.isPresent()) throw new ConsultaValidationException
                ("O paciente já tem um Consulta nesta data");
    }

    // Regra 4: Valida se o médico já possui outra Consulta agendada na mesma data/hora;
    public void validarDisponibilidadeMedico(Long idMedico, LocalDateTime data) {
        var consulta  = consultaRepository
                .findConsultaByMedicoAndDataHora(idMedico, data);
                if(consulta.isPresent()) throw new ConsultaValidationException
                        ("O medico já tem um Consulta nesta data/hora");
    }

}