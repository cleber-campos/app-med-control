package med.api.adapters.integration.medico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import med.api.adapters.integration.endereco.EnderecoRequestCreateDTO;
import med.api.domain.models.Especialidade;

@JsonIgnoreProperties
public record MedicoRequestUpdateDTO(
        @NotNull
        Long id,
        @NotNull
        String nome,
        String telefone,
        Especialidade especialidade,
        EnderecoRequestCreateDTO endereco) {
}
