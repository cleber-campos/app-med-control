package app.dtos.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record UsuarioResponseDTO(
        Long id,
        String login,
        Boolean status){
}
