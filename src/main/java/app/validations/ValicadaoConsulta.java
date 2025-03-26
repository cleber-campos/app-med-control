package app.validations;

import app.dtos.consulta.ConsultaRequestCreateDTO;

public interface ValicadaoConsulta {

    void validar(ConsultaRequestCreateDTO request);

}
