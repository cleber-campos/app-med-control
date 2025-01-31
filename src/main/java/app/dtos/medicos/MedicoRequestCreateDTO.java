package app.dtos.medicos;

import app.models.Especialidade;
import app.dtos.endereco.EnderecoRequestCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestCreateDTO(
        @NotBlank (message = "O nome e obrigatorio")
        String nome,

        @NotBlank (message = "O email e obrigatorio")
        @Email (message = "Formato de email invalido")
        String email,

        @NotBlank (message = "O telefone e obrigatorio")
        String telefone,

        @NotBlank (message = "O crm e obrigatorio")
        @Pattern (regexp = "\\d{4,6}", message = "Formato do CRM invalido")
        String crm,

        @NotNull (message = "A especialidade e obrigatoria")
        Especialidade especialidade,

        @NotNull (message = "Dados de endereco sao obrigatorios")
        @Valid EnderecoRequestCreateDTO endereco) {
}
