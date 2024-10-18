package med.api.adapters.integration.agendamento;

import jakarta.persistence.EntityNotFoundException;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestCreateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestUpdateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoResponseDTO;
import med.api.adapters.integration.medico.MedicoService;
import med.api.adapters.integration.paciente.PacienteService;
import med.api.adapters.repository.jpa.AgendamentoRepository;
import med.api.domain.models.Agendamento;
import med.api.domain.models.Medico;
import med.api.domain.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    AgendamentoRepository agendamentoRepository;
    @Autowired
    MedicoService medicoService;
    @Autowired
    PacienteService pacienteService;

    public void cadastrarAgendamento(AgendamentoRequestCreateDTO
                                             agendamentoRequestCreateDTO) throws Exception {
        var medico = medicoService.obterMedicoPorId(
                agendamentoRequestCreateDTO.idMedico());
        // se o medico vier vazio
        // obterMedicoDisponivel();

        var paciente = pacienteService.obterPacientePorId(
                agendamentoRequestCreateDTO.idPaciente());

        var dataHora = agendamentoRequestCreateDTO.dataHora();

        validarRegrasInclusaoAgendamento(medico, paciente, dataHora);

        agendamentoRepository.save(new Agendamento(paciente, medico, dataHora));
    }

    public AgendamentoResponseDTO obterAgendamentoPorId(Long id) {
        return converteAgendamentoResponseDTO(
                agendamentoRepository.findById(id));
    }

    public void alterarAgendamentoPorId(Long id, AgendamentoRequestUpdateDTO agendamentoUpdateDTO) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("O ID do agendamento não pode ser nulo.");
        }
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        Medico medico = agendamento.getMedico();
        if(agendamentoUpdateDTO.idMedico() != null) {
            medico = medicoService.obterMedicoPorId(agendamentoUpdateDTO.idMedico());
        }

        Paciente paciente = agendamento.getPaciente();
        if(agendamentoUpdateDTO.idPaciente() != null) {
            paciente = pacienteService.obterPacientePorId(agendamentoUpdateDTO.idPaciente());
        }

        //Validar entrada Agendamento
        validarRegrasAlteracaoAgendamento(medico, paciente, agendamentoUpdateDTO.dataHora());

        //Atualiza o agendamento
        agendamento.setDataHora(agendamentoUpdateDTO.dataHora());
        agendamento.setMedico(medico);
        agendamento.setPaciente(paciente);

        //Salva agendamento atualizado
        agendamentoRepository.save(agendamento);
    }

    public void deletarAgendamentoPorId(Long id) {
        agendamentoRepository.deleteById(id);
    }

    //revisar
    public Page<AgendamentoResponseDTO> obterListaPaginadaDeAgendamentos(
            Pageable paginacao) {
        return null;
    }

    public AgendamentoResponseDTO converteAgendamentoResponseDTO(
            Optional<Agendamento> agendamento){
        if(agendamento.isPresent()){
            var a = agendamento.get();
            var medico = medicoService.converteMedicoResponseDTOAgend(
                    a.getMedico());
            var paciente = pacienteService.convertePacienteResponseDTOAgend(
                    a.getPaciente());
            return new AgendamentoResponseDTO(a.getId(), a.getDataHora(),
                    paciente, medico);
        }
        return null;
    }

    private void obterMedicoDisponivel(){

    }

    private void validarRegrasInclusaoAgendamento(Medico medico, Paciente paciente,
                                                  LocalDateTime dataHora) throws Exception {
        validarHorarioFuncionamento(dataHora);
        validarAntecedenciaMinima(dataHora);
        validarDisponibilidadePaciente(paciente, dataHora);
        validarDisponibilidadeMedico(medico, dataHora);
    }

    private void validarRegrasAlteracaoAgendamento(Medico medico, Paciente paciente,
                                                  LocalDateTime dataHora) throws Exception {
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