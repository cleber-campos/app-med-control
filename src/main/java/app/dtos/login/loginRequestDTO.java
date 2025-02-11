package app.dtos.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record loginRequestDTO(
        @NotBlank(message = "O Login e obrigatorio")
        @Email(message = "Formato de email invalido")
        String login,
        @NotNull(message = "A senha e obrigatoria")
        String senha){
}
