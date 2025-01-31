package app.controllers;

import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.services.MedicoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/medicos")
public class MedicoController {

    @Autowired
    MedicoService medicoService;

    @PostMapping
    @Transactional
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestCreateDTO
                                                      medicoRequestDTO, UriComponentsBuilder uriBuilder) {
        var medicoResponseDTO = medicoService.cadastrarMedico(medicoRequestDTO);
        var uri = uriBuilder.path("/medicos/{idMedico}").buildAndExpand(medicoResponseDTO.id()).toUri();
        return  ResponseEntity.created(uri).body(medicoResponseDTO);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<MedicoResponseDTO> consultar(@PathVariable Long id) {
        var medicoResponseDTO = medicoService.obterMedicoResponseDTOPorId(id);
        return ResponseEntity.ok(medicoResponseDTO);
    }

    @PutMapping ("/{id}")
    @Transactional
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid
    MedicoRequestUpdateDTO medicoRequestDTO) {
        var medicoResponseDTO = medicoService.alterarMedicoPorId(id, medicoRequestDTO);
        return ResponseEntity.ok(medicoResponseDTO);
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        medicoService.inativarMedicoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listar(Pageable paginacao) {
        var page = medicoService.obterListaDeMedicos(paginacao);
        return ResponseEntity.ok(page);
    }

}
