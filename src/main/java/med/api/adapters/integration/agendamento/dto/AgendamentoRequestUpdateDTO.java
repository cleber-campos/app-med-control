package med.api.adapters.integration.agendamento.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import med.api.domain.models.Medico;
import med.api.domain.models.Paciente;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AgendamentoRequestUpdateDTO(
        @NotNull
        Long idPaciente,
        Long idMedico,
        LocalDateTime dataHora){
}
