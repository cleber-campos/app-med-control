package med.api.adapters.integration.paciente.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record PacienteResponseDTOAgend(
        Long idPaciente,
        String nome,
        String cpf) {
}
