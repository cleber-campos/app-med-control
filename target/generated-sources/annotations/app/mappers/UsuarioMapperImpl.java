package app.mappers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-31T18:14:04-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toEntity(UsuarioCreateDTO usuarioRequest) {
        if ( usuarioRequest == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setLogin( usuarioRequest.login() );
        usuario.setSenha( usuarioRequest.senha() );

        usuario.setStatus( true );

        return usuario;
    }

    @Override
    public void toUpdateEntity(UsuarioUpdateDTO usuarioRequest, Usuario usuario) {
        if ( usuarioRequest == null ) {
            return;
        }

        if ( usuarioRequest.login() != null ) {
            usuario.setLogin( usuarioRequest.login() );
        }
        if ( usuarioRequest.senha() != null ) {
            usuario.setSenha( usuarioRequest.senha() );
        }
    }

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String login = null;
        Boolean status = null;

        id = usuario.getId();
        login = usuario.getLogin();
        status = usuario.getStatus();

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO( id, login, status );

        return usuarioResponseDTO;
    }

    @Override
    public List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios) {
        if ( usuarios == null ) {
            return null;
        }

        List<UsuarioResponseDTO> list = new ArrayList<UsuarioResponseDTO>( usuarios.size() );
        for ( Usuario usuario : usuarios ) {
            list.add( toResponseDTO( usuario ) );
        }

        return list;
    }
}
