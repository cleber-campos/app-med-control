package app.domain.usuarios.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioRequestUpdateDTO(
        @Email(message = "Formato de email invalido")
        String login,
        String senha){
}
