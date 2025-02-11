package app.dtos.consulta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestCreateDTO(
        @NotNull (message = "O id do paciente e obrigatorio")
        Long idPaciente,
        Long idMedico,
        @NotNull (message = "A data da consulta e obrigatoria")
        LocalDateTime dataConsulta){
}

