package app.dtos.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioUpdateDTO(
        @Email(message = "Formato de email invalido")
        String login,
        String senha){
}
