package app.domain.consultas.dto;

import app.domain.medicos.dto.MedicoResponseDTO;
import app.domain.pacientes.dto.PacienteResponseDTO;
import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        LocalDateTime dataConsulta,
        PacienteResponseDTO paciente,
        MedicoResponseDTO medico) {

}
