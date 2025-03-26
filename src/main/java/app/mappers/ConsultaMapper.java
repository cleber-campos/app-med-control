package app.mappers;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.models.Consulta;
import app.models.Medico;

public interface ConsultaMapper {

    Consulta toEntity(ConsultaRequestCreateDTO requestDTO);
    Consulta updateFromDTO(ConsultaRequestUpdateDTO requestDTO, Medico medico);
    ConsultaResponseDTO toResponseDTO(Consulta consulta);

}

