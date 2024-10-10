package med.api.adapters.integration.medico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import med.api.domain.models.Especialidade;

@JsonIgnoreProperties
public record MedicoResponseDTO(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade,
        Boolean ativo) {
}
