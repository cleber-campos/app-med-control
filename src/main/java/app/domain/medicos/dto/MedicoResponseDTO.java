package app.domain.medicos.dto;

import app.domain.medicos.model.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record MedicoResponseDTO(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade,
        Boolean status) {
}
