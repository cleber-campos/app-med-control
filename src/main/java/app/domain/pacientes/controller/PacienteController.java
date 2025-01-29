package app.domain.pacientes.controller;

import app.domain.pacientes.dto.PacienteRequestCreateDTO;
import app.domain.pacientes.dto.PacienteRequestUpdateDTO;
import app.domain.pacientes.dto.PacienteResponseDTO;
import app.domain.pacientes.service.PacienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/pacientes")
public class PacienteController {

    @Autowired
    PacienteService pacienteService;

    @PostMapping
    @Transactional
    public ResponseEntity<PacienteResponseDTO> cadastrar(
            @RequestBody @Valid PacienteRequestCreateDTO pacienteRequestDTO
            , UriComponentsBuilder uriBuilder) {
        var pacienteResponseDTO = pacienteService.cadastrarPaciente(pacienteRequestDTO);
        var uri = uriBuilder.path("/pacientes/{idPaciente}").buildAndExpand(pacienteResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(pacienteResponseDTO);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<PacienteResponseDTO> consultar(@PathVariable Long id) {
        var pacienteResponseDTO = pacienteService.obterPacienteResponseDTOPorId(id);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PacienteResponseDTO> atualizar(
            @PathVariable Long id, @RequestBody @Valid PacienteRequestUpdateDTO pacienteRequestDTO){
        var pacienteResponseDTO = pacienteService.alterarPaciente(id, pacienteRequestDTO);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        pacienteService.InativarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> listar(@PageableDefault Pageable paginacao) {
        var page = pacienteService.obterListaDePacientes(paginacao);
        return ResponseEntity.ok(page);
    }

}
