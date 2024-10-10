package med.api.adapters.integration.paciente.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.api.adapters.integration.endereco.EnderecoRequestCreateDTO;

public record PacienteRequestCreatedDTO(
        @NotBlank(message = "O nome e obrigatorio")
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String cpf,
        @NotNull
        @Valid
        EnderecoRequestCreateDTO endereco) {
}
