package app.dtos.consulta;

import app.dtos.medicos.MedicoResponseDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ConsultaResponseDTO{
    private Long id;
    private LocalDateTime dataConsulta;
    private Boolean Status;
    private PacienteResponseDTO paciente;
    private MedicoResponseDTO medico;
}