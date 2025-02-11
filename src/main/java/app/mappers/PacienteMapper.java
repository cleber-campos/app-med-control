package app.mappers;

import app.dtos.pacientes.PacienteRequestCreateDTO;
import app.dtos.pacientes.PacienteRequestUpdateDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import app.models.Paciente;

public interface PacienteMapper {

    Paciente toEntity(PacienteRequestCreateDTO requestDTO);
    Paciente updateFromDTO(PacienteRequestUpdateDTO requestDTO);
    PacienteResponseDTO toResponse(Paciente paciente);

}
