package app.mappers;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.models.Consulta;

public interface ConsultaMapper {

    Consulta toEntity(ConsultaRequestCreateDTO requestDTO);
    Consulta updateFromDTO(ConsultaRequestUpdateDTO requestDTO);
    ConsultaResponseDTO toResponseDTO(Consulta consulta);

}

