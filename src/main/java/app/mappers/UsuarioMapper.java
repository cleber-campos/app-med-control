package app.mappers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.models.Usuario;

public interface UsuarioMapper {

    Usuario toEntity(UsuarioCreateDTO requestDTO);
    Usuario updateFromDTO(UsuarioUpdateDTO requestDTO);
    UsuarioResponseDTO toResponseDTO(Usuario usuario);
}
