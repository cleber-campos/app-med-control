package app.domain.usuarios.service;

import app.domain.usuarios.dto.UsuarioRequestCreateDTO;
import app.domain.usuarios.dto.UsuarioRequestUpdateDTO;
import app.domain.usuarios.dto.UsuarioResponseDTO;
import app.domain.usuarios.model.Usuario;
import app.domain.usuarios.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestCreateDTO usuarioRequestDTO){
        var usuario = new Usuario(usuarioRequestDTO);
        usuarioRepository.save(usuario);
        return converteUsuarioDTO(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long idUsuario) {
    if (idUsuario == null) throw new IllegalArgumentException("O ID do usuario não pode ser nulo.");
    var usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        return converteUsuarioDTO(usuario);
    }

    public UsuarioResponseDTO alterarUsuarioPorId(Long idUsuario, UsuarioRequestUpdateDTO usuarioRequestDTO) {
        var usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        usuario.atualizaDados(usuarioRequestDTO);
        return buscarUsuarioPorId(idUsuario);
    }

    public void inativarUsuarioPorId(Long idUsuario) {
    if(idUsuario == null) throw new IllegalArgumentException("O ID do usuario nao pode ser nulo.");
    var usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    usuario.setStatus(false);
    usuarioRepository.save(usuario);
    }

    public Page<UsuarioResponseDTO> obterListaPaginadaDeUsuarios(Pageable paginacao) {
        return convertePageUsuarios(usuarioRepository.findAllByStatusTrue(paginacao));
    }

    public UsuarioResponseDTO converteUsuarioDTO(Usuario usuario){ //substituir por Mapper
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getStatus());
    }

    public Page<UsuarioResponseDTO> convertePageUsuarios(@PageableDefault(size = 10) Page<Usuario> usuarios){
        return usuarios.map(u -> new UsuarioResponseDTO(
                u.getId(),
                u.getLogin(),
                u.getStatus()));
    }

}
