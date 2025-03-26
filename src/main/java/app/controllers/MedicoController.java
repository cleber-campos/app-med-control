package app.controllers;

import app.dtos.PageDTO;
import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.services.MedicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> criar(@RequestBody @Valid MedicoRequestCreateDTO
        request, UriComponentsBuilder uriBuilder) {
        MedicoResponseDTO medico = medicoService.criarMedico(request);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return  ResponseEntity.created(uri).body(medico);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<MedicoResponseDTO> consultar(@PathVariable Long id) {
        MedicoResponseDTO medico = medicoService.buscarMedico(id);
        return ResponseEntity.ok(medico);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id,
        @RequestBody @Valid MedicoRequestUpdateDTO request) {
        MedicoResponseDTO medico = medicoService.atualizarMedico(id, request);
        return ResponseEntity.ok(medico);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        medicoService.inativarMedico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<MedicoResponseDTO>> listar(Pageable paginacao) {
        PageDTO<MedicoResponseDTO> page = medicoService.listarMedicos(paginacao);
        return ResponseEntity.ok(page);
    }

}
