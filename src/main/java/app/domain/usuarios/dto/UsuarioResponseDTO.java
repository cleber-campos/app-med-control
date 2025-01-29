package app.domain.usuarios.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record UsuarioResponseDTO(
        Long id,
        String login,
        Boolean status){
}
