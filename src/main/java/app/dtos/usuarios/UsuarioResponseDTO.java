package app.dtos.usuarios;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String login;
    private Boolean status;
}
