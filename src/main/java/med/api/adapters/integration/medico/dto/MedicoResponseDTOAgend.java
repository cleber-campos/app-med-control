package med.api.adapters.integration.medico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import med.api.domain.models.Especialidade;

@JsonIgnoreProperties
public record MedicoResponseDTOAgend(
            Long idMedico,
            String nome,
            String crm,
            Especialidade especialidade) {
}
