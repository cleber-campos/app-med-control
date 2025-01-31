package app.dtos.medicos;

import app.models.Especialidade;
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
