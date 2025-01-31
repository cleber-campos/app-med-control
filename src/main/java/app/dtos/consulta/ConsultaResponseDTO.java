package app.dtos.consulta;

import app.dtos.medicos.MedicoResponseDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        LocalDateTime dataConsulta,
        Boolean Status,
        PacienteResponseDTO paciente,
        MedicoResponseDTO medico) {

}
