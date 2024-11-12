package app.domain.consultas.service;

import app.domain.consultas.dto.ConsultaRequestCreateDTO;
import app.domain.consultas.dto.ConsultaRequestUpdateDTO;
import app.domain.consultas.dto.ConsultaResponseDTO;
import app.domain.consultas.model.Consulta;
import app.domain.consultas.repository.ConsultaRepository;
import app.domain.medicos.service.MedicoService;
import app.domain.medicos.model.Medico;
import app.domain.pacientes.model.Paciente;
import app.domain.pacientes.service.PacienteService;
import app.shared.exceptions.*;
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

    public void cadastrarConsulta(ConsultaRequestCreateDTO ConsultaRequestCreateDTO) {

        var medico = obterMedicoParaConsulta(ConsultaRequestCreateDTO);
        var paciente = obterPacienteParaConsulta(ConsultaRequestCreateDTO);
        LocalDateTime dataConsulta = ConsultaRequestCreateDTO.dataConsulta();

        // Validação de regras de negócio antes do Consulta
        validarRegrasInclusaoConsulta(medico, paciente, dataConsulta);

        Consulta novoConsulta = new Consulta(paciente, medico, dataConsulta);
        consultaRepository.save(novoConsulta);
    }

    public ConsultaResponseDTO obterConsultaPorId(Long id) {

        var Consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrado"));

        var medico = medicoService.obterMedicoResponseDTOPorId(Consulta.getMedico().getId());

        var paciente = pacienteService.obterPacienteResponseDTOPorId(Consulta.getPaciente().getId());

        return new ConsultaResponseDTO(Consulta.getId(), Consulta.getDataConsulta(),
                paciente, medico);
    }

    public void atualizarConsulta(Long id, ConsultaRequestUpdateDTO ConsultaUpdateDTO) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do Consulta não pode ser nulo.");
        }
        Consulta Consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrado"));

        Medico medico = Consulta.getMedico();
        if (ConsultaUpdateDTO.idMedico() != null) {
            medico = medicoService.obterMedicoPorId(ConsultaUpdateDTO.idMedico());
        }

        validarRegrasAlteracaoConsulta(medico, ConsultaUpdateDTO.dataConsulta());

        if (ConsultaUpdateDTO.dataConsulta() == null) {
            throw new DataHoraNotFoundException("Data do Consulta nao informada!");
        }

        // Atualiza dados do Consulta
        Consulta.setDataConsulta(ConsultaUpdateDTO.dataConsulta());
        Consulta.setMedico(medico);

        // Salva alteracoes
        consultaRepository.save(Consulta);
    }

    public void cancelarConsulta(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do Consulta não pode ser nulo.");
        }
        //verificar se o Consulta existe antes de deletar
        consultaRepository.deleteById(id);
    }

    public Page<ConsultaResponseDTO> listarConsultas(Pageable paginacao) {
        //paginacao ordenada
        Pageable paginacaoOrdenada = PageRequest.of(
                paginacao.getPageNumber(),
                paginacao.getPageSize(),
                Sort.by(Sort.Direction.ASC, "dataHora")
        );
        // Busca consultas no banco de dados de forma paginada e ordenada
        Page<Consulta> ConsultasPaginados = consultaRepository.findAll(paginacaoOrdenada);

        // Converte cada Consulta para ConsultaResponseDTO
        return ConsultasPaginados.map(
                a -> new ConsultaResponseDTO( a.getId(), a.getDataConsulta(),
                pacienteService.obterPacienteResponseDTOPorId(a.getPaciente().getId()),
                        medicoService.obterMedicoResponseDTOPorId(a.getMedico().getId()))
        );
    }

    public void notificaConsultaPaciente(){
    }

    private Paciente obterPacienteParaConsulta(ConsultaRequestCreateDTO ConsultaRequestCreateDTO) {
        var paciente = pacienteService.obterPacientePorId(ConsultaRequestCreateDTO.idPaciente());
        if(!paciente.getStatus()){ throw new PacienteInativeException("O paciente esta inativo."); }
        return paciente;
    }

    private Medico obterMedicoParaConsulta(ConsultaRequestCreateDTO ConsultaRequestCreateDTO) {
        Pageable limite = PageRequest.of(0, 1);  // Página 0, com tamanho 1 (apenas 1 médico)
        if(ConsultaRequestCreateDTO.idMedico() == null) {
            // Busca apenas um médico disponível disponivel no horario
            Page<Medico> medicoDisponivel = consultaRepository.
                    findMedicoDisponivel(ConsultaRequestCreateDTO.dataConsulta(), limite);

            // Verifica se encontrou algum médico disponível
            var medico = medicoDisponivel.stream().findFirst().orElseThrow(() ->
                    new MedicoNotFoundException("Nenhum médico disponível para o horário solicitado."));

            // Verifica se o medico esta ativo
            if(!medico.getStatus()){ throw new MedicoInativeException("O medico esta inativo."); }
            return medico;
        }

        var medico = medicoService.obterMedicoPorId(ConsultaRequestCreateDTO.idMedico());

        //melhorar verificacao para evitar duplicidade
        // Verifica se o medico esta ativo
        if(!medico.getStatus()){ throw new MedicoInativeException("O medico esta inativo."); }
        return medico;
    }

    private void validarRegrasInclusaoConsulta(Medico medico, Paciente paciente, LocalDateTime dataConsulta) {
        validarHorarioFuncionamento(dataConsulta);
        validarAntecedenciaMinima(dataConsulta);
        validarDisponibilidadePaciente(paciente, dataConsulta);
        validarDisponibilidadeMedico(medico, dataConsulta);
    }

    private void validarRegrasAlteracaoConsulta(Medico medico, LocalDateTime dataConsulta)
            throws ConsultaValidationException {
        validarHorarioFuncionamento(dataConsulta);
        validarAntecedenciaMinima(dataConsulta);
        validarDisponibilidadeMedico(medico, dataConsulta);
    }

    // Regra 1: Valida horário de funcionamento (segunda a sábado, 08:00 às 18:00)
    private void validarHorarioFuncionamento(LocalDateTime dataConsulta) throws ConsultaValidationException {
        DayOfWeek diaDaSemana = dataConsulta.getDayOfWeek();
        LocalTime horaConsulta = dataConsulta.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY ||
                horaConsulta.isBefore(LocalTime.of(8, 0)) ||
                horaConsulta.isAfter(LocalTime.of(18, 0))) {
            throw new ConsultaValidationException("A Consulta deve ser agendada entre segunda e sábado, das 08:00 às 18:00.");
        }
    }

    // Regra 2: Valida a antecedência mínima de 30 minutos
    private void validarAntecedenciaMinima(LocalDateTime dataConsulta) throws ConsultaValidationException {
        LocalDateTime agora = LocalDateTime.now();
        Duration diferencaTempo = Duration.between(agora, dataConsulta);
        if (diferencaTempo.toMinutes() < 30) {
            throw new ConsultaValidationException("A Consulta deve ser agendada com, no mínimo, 30 minutos de antecedência.");
        }
    }

    // Regra 3: Valida se o paciente já possui um Consulta no mesmo dia;
    public void validarDisponibilidadePaciente(Paciente paciente, LocalDateTime dataConsulta) {
        var ConsultaExistente = consultaRepository
                .findConsultaByPacienteAndData(paciente.getId(), dataConsulta);
        if(ConsultaExistente.isPresent()){
            throw new ConsultaValidationException("O paciente já tem um Consulta neste dia");
        }
    }

    // Regra 4: Valida se o médico já possui outra Consulta agendada na mesma data/hora;
    public void validarDisponibilidadeMedico(Medico medico, LocalDateTime dataConsulta) {
        var ConsultaExistente = consultaRepository
                .findConsultaByMedicoAndDataHora(medico.getId(), dataConsulta);
        if(ConsultaExistente.isPresent()){
            throw new ConsultaValidationException("O medico já tem um Consulta nesta data/hora");
        }
    }

}