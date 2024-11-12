package app.domain.pacientes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        Boolean status) {
}
