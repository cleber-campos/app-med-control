package app.domain.autentificacao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutentificacaoRequestDTO(
        @NotBlank(message = "O Login e obrigatorio")
        @Email(message = "Formato de email invalido")
        String login,
        @NotNull(message = "A senha e obrigatoria")
        String senha){
}
