package app.domain.medicos.dto;

import app.domain.medicos.model.Especialidade;
import app.domain.endereco.EnderecoRequestCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestCreateDTO(
        @NotBlank (message = "O nome e obrigatorio") String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Pattern(regexp = "\\d{4,6}") String crm,
        @NotNull Especialidade especialidade,
        @NotNull @Valid EnderecoRequestCreateDTO endereco) {
}
