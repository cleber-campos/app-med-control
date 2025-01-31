package app.dtos.consulta;

import app.models.MotivoCancelamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestCancelDTO(
        MotivoCancelamento motivoCancelamento){
}
