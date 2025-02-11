package app.mappers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toEntity(UsuarioCreateDTO requestDTO) {
        return Usuario.builder()
                .login(requestDTO.login())
                .senha(requestDTO.senha())
                .status(true)
                .build();
    }

    @Override
    public Usuario updateFromDTO(UsuarioUpdateDTO requestDTO) {
        return Usuario.builder()
                .login(requestDTO.login())
                .senha(requestDTO.senha())
                .build();
    }

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .login(usuario.getLogin())
                .status(usuario.getStatus())
                .build();
    }
}
