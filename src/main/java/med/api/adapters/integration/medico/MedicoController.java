package med.api.adapters.integration.medico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.api.adapters.integration.medico.dto.MedicoRequestCreateDTO;
import med.api.adapters.integration.medico.dto.MedicoRequestUpdateDTO;
import med.api.adapters.integration.medico.dto.MedicoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    MedicoService medicoService;

    @PostMapping
    @Transactional
    public void criarMedico(@RequestBody @Valid MedicoRequestCreateDTO medicoRequestCreateDTO) {
        medicoService.cadastrarMedico(medicoRequestCreateDTO);
    }

    @GetMapping ("/{idMedico}")
    public MedicoResponseDTO obterMedicoPorId(@PathVariable Long idMedico) {
        return medicoService.obterMedicoPorId(idMedico);
    }

    @PutMapping
    @Transactional
    public void atualizarMedicoPorId(@RequestBody @Valid MedicoRequestUpdateDTO medicoUpdateDTO) {
        medicoService.alterarMedicoPorId(medicoUpdateDTO);
    }

    @DeleteMapping ("/{idMedico}")
    @Transactional
    public void deletarMedicoPorId(@PathVariable Long idMedico) {
        medicoService.inativarMedicoPorId(idMedico);
    }

    @GetMapping
    public Page<MedicoResponseDTO> obterListaDeMedicos(Pageable paginacao) {
        return medicoService.obterListaPaginadaDeMedicosAtivos(paginacao);
    }

}
