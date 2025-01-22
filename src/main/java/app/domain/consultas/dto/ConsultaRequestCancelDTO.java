package app.domain.consultas.dto;

import app.domain.consultas.model.MotivoCancelamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsultaRequestCancelDTO(
        MotivoCancelamento motivoCancelamento){
}
