package app.controllers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.PageDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.services.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid
        UsuarioCreateDTO request, UriComponentsBuilder uriBuilder) {
        UsuarioResponseDTO usuario = usuarioService.criarUsuario(request);
        var uri = uriBuilder.path("/usuarios/{idUsuario}")
                .buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id){
        UsuarioResponseDTO usuario = usuarioService.buscarUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> alterar(
            @PathVariable Long id, @RequestBody @Valid UsuarioUpdateDTO request){
        UsuarioResponseDTO usuario = usuarioService.atualizarUsuario(id, request);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        usuarioService.inativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<UsuarioResponseDTO>> listar(Pageable paginacao) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(paginacao));
    }

}
