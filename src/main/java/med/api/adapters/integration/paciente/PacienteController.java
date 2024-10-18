package med.api.adapters.integration.paciente;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.api.adapters.integration.paciente.dto.PacienteRequestUpdateDTO;
import med.api.adapters.integration.paciente.dto.PacienteResponseDTO;
import med.api.adapters.integration.paciente.dto.PacienteRequestCreatedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pacientes")
public class PacienteController {

    @Autowired
    PacienteService pacienteService;

    @PostMapping
    @Transactional
    public void cadastrarPaciente(@RequestBody @Valid PacienteRequestCreatedDTO pacienteRequestCreatedDTO) {
        pacienteService.cadastrarPaciente(pacienteRequestCreatedDTO);
    }

    @GetMapping ("/{idPaciente}")
    public PacienteResponseDTO obterPacienteResponseDTOPorId(@PathVariable Long idPaciente) {
        return pacienteService.obterPacienteResponseDTOPorId(idPaciente);
    }

    @PutMapping
    @Transactional
    public void atualizarPacienteporId(@RequestBody @Valid PacienteRequestUpdateDTO pacienteUpdateDTO){
        pacienteService.alterarPacientePorId(pacienteUpdateDTO);
    }

    @DeleteMapping ("/{idPaciente}")
    @Transactional
    public void inativarPaciente(@PathVariable Long idPaciente){
        pacienteService.InativarPacientePorId(idPaciente);
    }

    @GetMapping
    public Page<PacienteResponseDTO> obterListaDePacientesAtivos(@PageableDefault(size = 10) Pageable paginacao) {
        return pacienteService.obterListaDePacientes(paginacao);
    }

}
