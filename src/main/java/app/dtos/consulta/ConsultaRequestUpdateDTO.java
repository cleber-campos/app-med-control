package app.dtos.consulta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestUpdateDTO(
        Long idMedico,
        LocalDateTime dataConsulta){
}
