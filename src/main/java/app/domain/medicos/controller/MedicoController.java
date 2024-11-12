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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/medicos")
public class MedicoController {

    @Autowired
    MedicoService medicoService;

    @PostMapping
    @Transactional
    public void cadastrarMedico(@RequestBody @Valid MedicoRequestCreateDTO medicoRequestCreateDTO) {
        medicoService.cadastrarMedico(medicoRequestCreateDTO);
    }

    @GetMapping ("/{idMedico}")
    public MedicoResponseDTO obterMedicoPorId(@PathVariable Long idMedico) {
        return medicoService.obterMedicoResponseDTOPorId(idMedico);
    }

    @PutMapping ("/{idMedico}")
    @Transactional
    public void atualizarMedico(@PathVariable Long idMedico, @RequestBody @Valid MedicoRequestUpdateDTO medicoUpdateDTO) {
        medicoService.alterarMedicoPorId(idMedico, medicoUpdateDTO);
    }

    @DeleteMapping ("/{idMedico}")
    @Transactional
    public void inativarMedico(@PathVariable Long idMedico) {
        medicoService.inativarMedicoPorId(idMedico);
    }

    @GetMapping
    public Page<MedicoResponseDTO> listarMedicos(Pageable paginacao) {
        return medicoService.obterListaPaginadaDeMedicosAtivos(paginacao);
    }

}
