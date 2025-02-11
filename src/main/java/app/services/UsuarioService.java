package app.services;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.PageDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.mappers.UsuarioMapper;
import app.models.Usuario;
import app.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper
            usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO request){
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setSenha(passwordEncoder.encode(request.senha())); //encriptar senha
        var usuarioCriado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioCriado);
    }

    public UsuarioResponseDTO buscarUsuario(Long id) {
    if (id == null) throw new IllegalArgumentException("O ID do usuario não pode ser nulo.");
    var usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO request) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        var usuarioAlterado = usuarioMapper.updateFromDTO(request);
        if (request.senha() != null &&
                !passwordEncoder.matches(request.senha(), usuarioAlterado.getSenha())) {
            usuarioAlterado.setSenha(passwordEncoder.encode(request.senha()));
        }
        usuarioRepository.save(usuarioAlterado);
        return usuarioMapper.toResponseDTO(usuarioAlterado);
    }

    @Transactional
    public void inativarUsuario(Long id) {
    if(id == null) throw new IllegalArgumentException("O ID do usuario nao pode ser nulo.");
    var usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    if (!usuario.getStatus()) throw new IllegalStateException("Usuário já está inativo.");
    usuario.setStatus(false);
    }

    public Usuario buscarUsuarioPorLogin(String login){
        return (Usuario) usuarioRepository.findByLogin(login);
    }

    public PageDTO<UsuarioResponseDTO> listarUsuarios(Pageable paginacao) {
        var usuarios = usuarioRepository.findAllByStatusTrue(paginacao);
        var usuariosPageDTO = usuarios.map(usuarioMapper::toResponseDTO);
        return new PageDTO<>(usuariosPageDTO);
    }

}

