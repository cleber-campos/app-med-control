package med.api.adapters.integration.agendamento.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AgendamentoRequestCreateDTO (
        @NotNull
        Long idPaciente,
        Long idMedico,
        @NotNull
        LocalDateTime dataHora){
}
