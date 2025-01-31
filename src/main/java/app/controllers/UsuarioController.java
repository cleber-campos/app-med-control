package app.controllers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.usuarios.UsuarioPageDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.services.UsuarioService;
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
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody @Valid UsuarioCreateDTO usuarioRequestDTO,
            UriComponentsBuilder uriBuilder) {
        var usuarioResponseDTO = usuarioService.criarUsuario(usuarioRequestDTO);
        var uri = uriBuilder.path("/usuarios/{idUsuario}").buildAndExpand(usuarioResponseDTO.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuarioResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id){
        var usuarioResponseDTO = usuarioService.buscarUsuario(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> alterar(
            @PathVariable Long id, @RequestBody @Valid UsuarioUpdateDTO usuarioRequestDTO){
        var usuarioResponseDTO = usuarioService.atualizarUsuario(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        usuarioService.inativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UsuarioPageDTO> listar(Pageable paginacao){
        var page = usuarioService.buscarListaDeUsuarios(paginacao);
        return ResponseEntity.ok(page);
    }

}
