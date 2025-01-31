package app.dtos.consulta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestUpdateDTO(
        Long idPaciente,
        Long idMedico,
        LocalDateTime dataConsulta){
}
