package app.domain.medicos.controller;

import app.domain.medicos.dto.MedicoRequestCreateDTO;
import app.domain.medicos.dto.MedicoRequestUpdateDTO;
import app.domain.medicos.dto.MedicoResponseDTO;
import app.domain.medicos.service.MedicoService;
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
    public ResponseEntity<MedicoResponseDTO> cadastrarMedico(@RequestBody @Valid MedicoRequestCreateDTO
                                                      medicoRequestCreateDTO, UriComponentsBuilder uriBuilder) {
        var medicoResponseDTO = medicoService.cadastrarMedico(medicoRequestCreateDTO);
        var uri = uriBuilder.path("/medicos/{idMedico}").buildAndExpand(medicoResponseDTO.id()).toUri();
        return  ResponseEntity.created(uri).body(medicoResponseDTO);
    }

    @GetMapping ("/{idMedico}")
    public ResponseEntity<MedicoResponseDTO> obterMedicoPorId(@PathVariable Long idMedico) {
        var medicoResponseDTO = medicoService.obterMedicoResponseDTOPorId(idMedico);
        return ResponseEntity.ok(medicoResponseDTO);
    }

    @PutMapping ("/{idMedico}")
    @Transactional
    public ResponseEntity<MedicoResponseDTO> atualizarMedico(@PathVariable Long idMedico, @RequestBody @Valid
    MedicoRequestUpdateDTO medicoUpdateDTO) {
        var medicoResponseDTO = medicoService.alterarMedicoPorId(idMedico, medicoUpdateDTO);
        return ResponseEntity.ok(medicoResponseDTO);
    }

    @DeleteMapping ("/{idMedico}")
    @Transactional
    public ResponseEntity<Void> inativarMedico(@PathVariable Long idMedico) {
        medicoService.inativarMedicoPorId(idMedico);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listarMedicos(Pageable paginacao) {
        var page = medicoService.obterListaPaginadaDeMedicosAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

}
