package app.controllers;

import app.dtos.PageDTO;
import app.dtos.pacientes.PacienteRequestCreateDTO;
import app.dtos.pacientes.PacienteRequestUpdateDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import app.services.PacienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid
        PacienteRequestCreateDTO request, UriComponentsBuilder uriBuilder) {
        PacienteResponseDTO paciente = pacienteService.criarPaciente(request);
        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(
                paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(paciente);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<PacienteResponseDTO> consultar(@PathVariable Long id) {
        PacienteResponseDTO paciente = pacienteService.buscarPaciente(id);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(
            @PathVariable Long id, @RequestBody @Valid PacienteRequestUpdateDTO request){
        PacienteResponseDTO paciente = pacienteService.atualizarPaciente(id, request);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        pacienteService.inativarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<PacienteResponseDTO>> listar(@PageableDefault Pageable paginacao) {
        PageDTO<PacienteResponseDTO> page = pacienteService.listarPacientes(paginacao);
        return ResponseEntity.ok(page);
    }

}
