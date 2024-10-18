package med.api.adapters.integration.medico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import med.api.adapters.integration.endereco.EnderecoRequestCreateDTO;
import med.api.domain.models.Especialidade;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestCreateDTO(
        @NotBlank (message = "O nome e obrigatorio")
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid
        EnderecoRequestCreateDTO endereco) {
}
