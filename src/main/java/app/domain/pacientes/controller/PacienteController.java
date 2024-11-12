package app.domain.pacientes.controller;

import app.domain.pacientes.dto.PacienteRequestCreatedDTO;
import app.domain.pacientes.dto.PacienteRequestUpdateDTO;
import app.domain.pacientes.dto.PacienteResponseDTO;
import app.domain.pacientes.service.PacienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public PacienteResponseDTO obterPacientePorId(@PathVariable Long idPaciente) {
        return pacienteService.obterPacienteResponseDTOPorId(idPaciente);
    }

    @PutMapping("/{idPaciente}")
    @Transactional
    public void atualizarPaciente(@PathVariable Long idPaciente, @RequestBody @Valid PacienteRequestUpdateDTO pacienteUpdateDTO){
        pacienteService.alterarPaciente(idPaciente, pacienteUpdateDTO);
    }

    @DeleteMapping ("/{idPaciente}")
    @Transactional
    public void inativarPaciente(@PathVariable Long idPaciente){
        pacienteService.InativarPaciente(idPaciente);
    }

    @GetMapping
    public Page<PacienteResponseDTO> listarPacientes(@PageableDefault(size = 10) Pageable paginacao) {
        return pacienteService.obterListaDePacientes(paginacao);
    }

}
