package app.mappers;

import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.models.Medico;


public interface MedicoMapper {

    Medico toEntity(MedicoRequestCreateDTO requestDTO);
    Medico updateFromDTO(MedicoRequestUpdateDTO requestDTO);
    MedicoResponseDTO toResponseDTO(Medico medico);

}
