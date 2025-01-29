package app.domain.consultas.service;

import app.domain.consultas.dto.ConsultaRequestCreateDTO;
import app.domain.consultas.dto.ConsultaRequestUpdateDTO;
import app.domain.consultas.dto.ConsultaResponseDTO;
import app.domain.consultas.model.Consulta;
import app.domain.consultas.model.MotivoCancelamento;
import app.domain.consultas.repository.ConsultaRepository;
import app.domain.medicos.service.MedicoService;
import app.domain.medicos.model.Medico;
import app.domain.pacientes.service.PacienteService;
import app.shared.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ConsultaService {

    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    MedicoService medicoService;
    @Autowired
    PacienteService pacienteService;

    @Transactional
    public ConsultaResponseDTO cadastrarConsulta(ConsultaRequestCreateDTO consultaRequestDTO) {
        var dataConsulta = consultaRequestDTO.dataConsulta();
        var paciente = pacienteService.obterPacientePorId(consultaRequestDTO.idPaciente());
        if(consultaRequestDTO.idMedico() == null) {
            var medico = buscarMedicoDisponivel(dataConsulta);
            validarRegrasInclusaoConsulta(medico.getId(), consultaRequestDTO.idPaciente(), dataConsulta);
            var consulta = new Consulta(paciente, medico, dataConsulta);
            consultaRepository.save(consulta);
            return obterConsultaPorId(consulta.getId());
       }
        validarRegrasInclusaoConsulta(consultaRequestDTO.idMedico(), consultaRequestDTO.idPaciente(), dataConsulta);
        var medico = medicoService.obterMedicoPorId(consultaRequestDTO.idMedico());
        var consulta = new Consulta(paciente, medico, dataConsulta);
        consultaRepository.save(consulta);
        return obterConsultaPorId(consulta.getId());
    }

    @Transactional
    public ConsultaResponseDTO atualizarConsulta(Long idConsulta, ConsultaRequestUpdateDTO consultaRequestDTO) {
        if (idConsulta == null) throw new IllegalArgumentException("O ID da Consulta não pode ser nulo.");

        var consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado"));

        var dataConsulta = consultaRequestDTO.dataConsulta();

        if (consultaRequestDTO.dataConsulta() == null) dataConsulta = consulta.getDataConsulta();

        var idMedico = consultaRequestDTO.idMedico();

        if (consultaRequestDTO.idMedico() == null) idMedico = consulta.getMedico().getId();
        validarRegrasAlteracaoConsulta(idMedico, dataConsulta);
        consulta.setDataConsulta(consultaRequestDTO.dataConsulta());

        var medico = medicoService.obterMedicoPorId(idMedico);
        consulta.setMedico(medico);
        consultaRepository.save(consulta);
        return obterConsultaPorId(consulta.getId());
    }

    public ConsultaResponseDTO obterConsultaPorId(Long idConsulta) {
        var Consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado"));
        var medico = medicoService.obterMedicoResponseDTOPorId(Consulta.getMedico().getId());
        var paciente = pacienteService.obterPacienteResponseDTOPorId(Consulta.getPaciente().getId());
        return new ConsultaResponseDTO(Consulta.getId(), Consulta.getDataConsulta(),
                Consulta.getStatus(), paciente, medico);
    }

    @Transactional
    public void cancelarConsulta(Long idConsulta, MotivoCancelamento motivoCancelamento) {
        if (idConsulta == null) throw new IllegalArgumentException("O ID do Consulta não pode ser nulo.");
        var consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado"));
        consulta.setMotivoCancelamento(motivoCancelamento);
        consulta.setStatus(false);
        consultaRepository.save(consulta);
    }

    public Page<ConsultaResponseDTO> listarConsultas(Pageable paginacao) {
        Pageable paginacaoOrdenada = PageRequest.of(
                paginacao.getPageNumber(), paginacao.getPageSize(),
                Sort.by(Sort.Direction.ASC, "dataConsulta"));
        Page<Consulta> ConsultasPaginadas = consultaRepository.findAll(paginacaoOrdenada);
        return ConsultasPaginadas.map(a -> new ConsultaResponseDTO(
                a.getId(),
                a.getDataConsulta(),
                a.getStatus(),
                pacienteService.obterPacienteResponseDTOPorId(a.getPaciente().getId()),
                medicoService.obterMedicoResponseDTOPorId(a.getMedico().getId())));
    }

    private Medico buscarMedicoDisponivel(LocalDateTime dataConsulta) {
        var medicos = medicoService.obterListaMedicosAtivos();
        for (Medico medico : medicos) {
            boolean consultaExistente = consultaRepository
                    .findConsultaByMedicoAndDataHora(medico.getId(), dataConsulta).isPresent();
            if (!consultaExistente) return medico;
        }
        throw new EntityNotFoundException("Nenhum médico disponível na data e hora especificada.");
    }

    private void validarRegrasInclusaoConsulta(Long idMedico, Long idPaciente, LocalDateTime dataConsulta) {
        var paciente = pacienteService.obterPacientePorId(idPaciente);
        if(!paciente.getStatus()){ throw new PacienteInativeException("O paciente esta inativo."); }
        validarDisponibilidadePaciente(idPaciente, dataConsulta);

        var medico = medicoService.obterMedicoPorId(idMedico);
        if(!medico.getStatus()){ throw new MedicoInativeException("O medico esta inativo.");}
        validarDisponibilidadeMedico(idMedico, dataConsulta);
        validarHorarioFuncionamento(dataConsulta);
        validarAntecedenciaMinima(dataConsulta);
    }

    private void validarRegrasAlteracaoConsulta(Long idMedico, LocalDateTime dataConsulta) {
        var medico = medicoService.obterMedicoPorId(idMedico);
        if(!medico.getStatus()){ throw new MedicoInativeException("O medico esta inativo.");}
        validarDisponibilidadeMedico(idMedico, dataConsulta);
        validarHorarioFuncionamento(dataConsulta);
        validarAntecedenciaMinima(dataConsulta);
    }

    // Regra 1: Valida horário de funcionamento (segunda a sábado, 08:00 às 18:00)
    private void validarHorarioFuncionamento(LocalDateTime dataConsulta) {
        DayOfWeek diaDaSemana = dataConsulta.getDayOfWeek();
        LocalTime horaConsulta = dataConsulta.toLocalTime();
        if (diaDaSemana == DayOfWeek.SUNDAY ||
                horaConsulta.isBefore(LocalTime.of(8, 0)) ||
                horaConsulta.isAfter(LocalTime.of(18, 0))) {
            throw new ConsultaValidationException
                    ("A Consulta deve ser agendada entre segunda e sábado, das 08:00 às 18:00.");
        }
    }

    // Regra 2: Valida a antecedência mínima de 30 minutos
    private void validarAntecedenciaMinima(LocalDateTime dataConsulta) {
        LocalDateTime agora = LocalDateTime.now();
        Duration diferencaTempo = Duration.between(agora, dataConsulta);
        if (diferencaTempo.toMinutes() < 30) throw new ConsultaValidationException
                ("A Consulta deve ser agendada com, no mínimo, 30 minutos de antecedência.");
    }

    // Regra 3: Valida se o paciente já possui um Consulta no mesmo dia;
    public void validarDisponibilidadePaciente(Long idPaciente, LocalDateTime dataConsulta) {
        var consulta = consultaRepository.findConsultaByPacienteAndData(idPaciente, dataConsulta);
        if(consulta.isPresent()) throw new ConsultaValidationException
                ("O paciente já tem um Consulta nesta data");
    }

    // Regra 4: Valida se o médico já possui outra Consulta agendada na mesma data/hora;
    public void validarDisponibilidadeMedico(Long idMedico, LocalDateTime dataConsulta) {
        var consulta  = consultaRepository.findConsultaByMedicoAndDataHora(idMedico, dataConsulta);
                if(consulta.isPresent()) throw new ConsultaValidationException
                        ("O medico já tem um Consulta nesta data/hora");
    }

}