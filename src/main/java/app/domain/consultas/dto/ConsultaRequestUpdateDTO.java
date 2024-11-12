package app.domain.consultas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestUpdateDTO(
        @NotNull
        Long idPaciente,
        Long idMedico,
        LocalDateTime dataConsulta){
}
