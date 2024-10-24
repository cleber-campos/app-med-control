package med.api.adapters.integration.agendamento;

import jakarta.persistence.EntityNotFoundException;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestCreateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestUpdateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoResponseDTO;
import med.api.adapters.integration.medico.MedicoService;
import med.api.adapters.integration.paciente.PacienteService;
import med.api.adapters.repository.jpa.AgendamentoRepository;
import med.api.domain.exceptions.MedicoNotFoundException;
import med.api.domain.models.Agendamento;
import med.api.domain.models.Medico;
import med.api.domain.models.Paciente;
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
public class AgendamentoService {

    @Autowired
    AgendamentoRepository agendamentoRepository;
    @Autowired
    MedicoService medicoService;
    @Autowired
    PacienteService pacienteService;

    public void cadastrarAgendamento(AgendamentoRequestCreateDTO agendamentoRequestCreateDTO) throws Exception {

        var medico = obterMedicoParaAgendamento(agendamentoRequestCreateDTO);
        var paciente = obterPacienteParaAgendamento(agendamentoRequestCreateDTO);
        LocalDateTime dataHora = agendamentoRequestCreateDTO.dataHora();

        // Validação de regras de negócio antes do agendamento
        validarRegrasInclusaoAgendamento(medico, paciente, dataHora);

        Agendamento novoAgendamento = new Agendamento(paciente, medico, dataHora);
        agendamentoRepository.save(novoAgendamento);
    }

    public AgendamentoResponseDTO obterAgendamentoPorId(Long id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        var medico = medicoService.converteMedicoResponseDTOAgend(agendamento.getMedico());
        var paciente = pacienteService.convertePacienteResponseDTOAgend(agendamento.getPaciente());
        return new AgendamentoResponseDTO(agendamento.getId(), agendamento.getDataHora(),
                paciente, medico);
    }

    public void alterarAgendamentoPorId(Long id, AgendamentoRequestUpdateDTO agendamentoUpdateDTO) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("O ID do agendamento não pode ser nulo.");
        }
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        Medico medico = agendamento.getMedico();
        if (agendamentoUpdateDTO.idMedico() != null) {
            medico = medicoService.obterMedicoPorId(agendamentoUpdateDTO.idMedico());
        }

        Paciente paciente = agendamento.getPaciente();
        if (agendamentoUpdateDTO.idPaciente() != null) {
            paciente = pacienteService.obterPacientePorId(agendamentoUpdateDTO.idPaciente());
        }

        validarRegrasAlteracaoAgendamento(medico, agendamentoUpdateDTO.dataHora());

        if (agendamentoUpdateDTO.dataHora() != null) {
            agendamento.setDataHora(agendamentoUpdateDTO.dataHora());
        }
        agendamento.setMedico(medico);
        agendamento.setPaciente(paciente);

        agendamentoRepository.save(agendamento);
    }

    public void deletarAgendamentoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do agendamento não pode ser nulo.");
        }
        agendamentoRepository.deleteById(id);
    }

    public Page<AgendamentoResponseDTO> obterListaPaginadaDeAgendamentos(Pageable paginacao) {
        //paginacao ordenada
        Pageable paginacaoOrdenada = PageRequest.of(
                paginacao.getPageNumber(),
                paginacao.getPageSize(),
                Sort.by(Sort.Direction.ASC, "dataHora")
        );
        // Busca agendamentos no banco de dados de forma paginada e ordenada
        Page<Agendamento> agendamentosPaginados = agendamentoRepository.findAll(paginacaoOrdenada);

        // Converte cada Agendamento para AgendamentoResponseDTO
        return agendamentosPaginados.map(
                a -> new AgendamentoResponseDTO( a.getId(), a.getDataHora(),
                pacienteService.convertePacienteResponseDTOAgend(a.getPaciente()),
                medicoService.converteMedicoResponseDTOAgend(a.getMedico()))
        );
    }

    private Paciente obterPacienteParaAgendamento(AgendamentoRequestCreateDTO agendamentoRequestCreateDTO) throws Exception {
        var paciente = pacienteService.obterPacientePorId(agendamentoRequestCreateDTO.idPaciente());
        if(!paciente.isAtivo()){ throw new Exception("O paciente esta inativo."); }
        return paciente;
    }

    private Medico obterMedicoParaAgendamento(AgendamentoRequestCreateDTO agendamentoRequestCreateDTO) throws Exception {
        Pageable limite = PageRequest.of(0, 1);  // Página 0, com tamanho 1 (apenas 1 médico)
        if(agendamentoRequestCreateDTO.idMedico() == null) {
            // Busca apenas um médico disponível disponivel no horario
            Page<Medico> medicoDisponivel = agendamentoRepository.
                    findMedicoDisponivel(agendamentoRequestCreateDTO.dataHora(), limite);

            // Verifica se encontrou algum médico disponível
            var medico = medicoDisponivel.stream().findFirst().orElseThrow(() ->
                    new MedicoNotFoundException("Nenhum médico disponível para o horário solicitado."));

            // Verifica se o medico esta ativo
            if(!medico.isAtivo()){ throw new Exception("O medico esta inativo."); }
            return medico;
        }

        var medico = medicoService.obterMedicoPorId(agendamentoRequestCreateDTO.idMedico());

        //melhorar verificacao para evitar duplicidade
        // Verifica se o medico esta ativo
        if(!medico.isAtivo()){ throw new Exception("O medico esta inativo."); }
        return medico;

    }

    private void validarRegrasInclusaoAgendamento(Medico medico, Paciente paciente, LocalDateTime dataHora) throws Exception {
        validarHorarioFuncionamento(dataHora);
        validarAntecedenciaMinima(dataHora);
        validarDisponibilidadePaciente(paciente, dataHora);
        validarDisponibilidadeMedico(medico, dataHora);
    }

    private void validarRegrasAlteracaoAgendamento(Medico medico, LocalDateTime dataHora) throws Exception {
        validarHorarioFuncionamento(dataHora);
        validarAntecedenciaMinima(dataHora);
        validarDisponibilidadeMedico(medico, dataHora);
    }

    // Regra 1: Valida horário de funcionamento (segunda a sábado, 08:00 às 18:00)
    private void validarHorarioFuncionamento(LocalDateTime dataHora) throws Exception {
        DayOfWeek diaDaSemana = dataHora.getDayOfWeek();
        LocalTime horaAgendamento = dataHora.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY ||
                horaAgendamento.isBefore(LocalTime.of(8, 0)) ||
                horaAgendamento.isAfter(LocalTime.of(18, 0))) {
            throw new Exception("A Agendamento deve ser agendada entre segunda e sábado, das 08:00 às 18:00.");
        }
    }

    // Regra 2: Valida a antecedência mínima de 30 minutos
    private void validarAntecedenciaMinima(LocalDateTime dataHora) throws Exception {
        LocalDateTime agora = LocalDateTime.now();
        Duration diferencaTempo = Duration.between(agora, dataHora);
        if (diferencaTempo.toMinutes() < 30) {
            throw new Exception("A Agendamento deve ser agendada com, no mínimo, 30 minutos de antecedência.");
        }
    }

    // Regra 3: Valida se o paciente já possui um agendamento no mesmo dia;
    public void validarDisponibilidadePaciente(Paciente paciente, LocalDateTime dataHora) {
        var agendamentoExistente = agendamentoRepository
                .findAgendamentoByPacienteAndData(paciente.getId(), dataHora);
        if(agendamentoExistente.isPresent()){
            throw new IllegalArgumentException("O paciente já tem um agendamento neste dia");
        }
    }

    // Regra 4: Valida se o médico já possui outra consulta agendada na mesma data/hora;
    public void validarDisponibilidadeMedico(Medico medico, LocalDateTime dataHora) {
        var agendamentoExistente = agendamentoRepository
                .findAgendamentoByMedicoAndDataHora(medico.getId(), dataHora);
        if(agendamentoExistente.isPresent()){
            throw new IllegalArgumentException("O medico já tem um agendamento nesta data/hora");
        }
    }

}