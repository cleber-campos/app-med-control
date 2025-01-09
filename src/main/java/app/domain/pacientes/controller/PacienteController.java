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
    public ResponseEntity cadastrarPaciente(@RequestBody @Valid PacienteRequestCreatedDTO pacienteRequestCreatedDTO
            , UriComponentsBuilder uriBuilder) {
        var pacienteResponseDTO = pacienteService.cadastrarPaciente(pacienteRequestCreatedDTO);
        var uri = uriBuilder.path("/pacientes/{idPaciente}").buildAndExpand(pacienteResponseDTO.id()).toUri();
        //201 - recurso criado
        return ResponseEntity.created(uri).body(pacienteResponseDTO);

    }

    @GetMapping ("/{idPaciente}")
    public ResponseEntity obterPacientePorId(@PathVariable Long idPaciente) {
        var pacienteResponseDTO = pacienteService.obterPacienteResponseDTOPorId(idPaciente);
        //200 - sucesso
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @PutMapping("/{idPaciente}")
    @Transactional
    public ResponseEntity atualizarPaciente(@PathVariable Long idPaciente, @RequestBody @Valid PacienteRequestUpdateDTO pacienteUpdateDTO){
        var pacienteResponseDTO = pacienteService.alterarPaciente(idPaciente, pacienteUpdateDTO);
        //200 - sucesso
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping ("/{idPaciente}")
    @Transactional
    public ResponseEntity inativarPaciente(@PathVariable Long idPaciente){
        pacienteService.InativarPaciente(idPaciente);
        //204 - Sem conteudo
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> listarPacientes(@PageableDefault(size = 10) Pageable paginacao) {
        var page = pacienteService.obterListaDePacientes(paginacao);
        //200 - sucesso
        return ResponseEntity.ok(page);
    }

}
