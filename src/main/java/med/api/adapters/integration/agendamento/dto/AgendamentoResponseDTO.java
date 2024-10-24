package med.api.adapters.integration.agendamento.dto;

import med.api.adapters.integration.medico.dto.MedicoResponseDTOAgend;
import med.api.adapters.integration.paciente.dto.PacienteResponseDTOAgend;
import java.time.LocalDateTime;


public record AgendamentoResponseDTO (
        Long idAgendamento,
        LocalDateTime dataHora,
        PacienteResponseDTOAgend paciente,
        MedicoResponseDTOAgend medico
        ){
}
