package app.domain.pacientes.dto;

import app.domain.endereco.EnderecoRequestCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteRequestCreatedDTO(
        @NotBlank (message = "O nome e obrigatorio") String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        @NotNull @Valid EnderecoRequestCreateDTO endereco
) {
}
