package app.domain.usuarios.controller;

import app.domain.usuarios.dto.UsuarioRequestCreateDTO;
import app.domain.usuarios.dto.UsuarioRequestUpdateDTO;
import app.domain.usuarios.dto.UsuarioResponseDTO;
import app.domain.usuarios.service.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody @Valid UsuarioRequestCreateDTO usuarioRequestDTO,
            UriComponentsBuilder uriBuilder) {
        var usuarioResponseDTO = usuarioService.cadastrarUsuario(usuarioRequestDTO);
        var uri = uriBuilder.path("/usuarios/{idUsuario}").buildAndExpand(usuarioResponseDTO.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuarioResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id){
        var usuarioResponseDTO = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> alterar(
            @PathVariable Long id, @RequestBody @Valid UsuarioRequestUpdateDTO usuarioRequestDTO){
        var usuarioResponseDTO = usuarioService.alterarUsuarioPorId(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        usuarioService.inativarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(Pageable paginacao){
        var page = usuarioService.obterListaPaginadaDeUsuarios(paginacao);
        return ResponseEntity.ok(page);
    }

}
