package app.domain.consultas.controller;

import app.domain.consultas.dto.ConsultaRequestCreateDTO;
import app.domain.consultas.dto.ConsultaRequestUpdateDTO;
import app.domain.consultas.dto.ConsultaResponseDTO;
import app.domain.consultas.model.MotivoCancelamento;
import app.domain.consultas.service.ConsultaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping ("api/consultas")
public class ConsultaController {

    @Autowired
    ConsultaService consultaService;

    @PostMapping
    @Transactional
    public ResponseEntity<ConsultaResponseDTO> cadastrarConsulta(@RequestBody @Valid
                                                                     ConsultaRequestCreateDTO ConsultaRequestCreateDTO,
                                            UriComponentsBuilder uriBuilder) {
        var consultaResponseDTO = consultaService.cadastrarConsulta(ConsultaRequestCreateDTO);
        var uri = uriBuilder.path("/consultas/{id}").buildAndExpand(consultaResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(consultaResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> obterConsultaPorId(@PathVariable Long id) {
        var consultaResponseDTO = consultaService.obterConsultaPorId(id);
        return ResponseEntity.ok(consultaResponseDTO);
    }

    @PutMapping ("/{id}")
    @Transactional
    public ResponseEntity<ConsultaResponseDTO> atualizarConsulta(@PathVariable Long id,
            @RequestBody @Valid ConsultaRequestUpdateDTO ConsultaUpdateDTO) {
        var consultaResponseDTO = consultaService.atualizarConsulta(id, ConsultaUpdateDTO);
        return ResponseEntity.ok(consultaResponseDTO);
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id, MotivoCancelamento motivoCancelamento) {
        consultaService.cancelarConsulta(id, motivoCancelamento);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ConsultaResponseDTO>> listarConsultas(Pageable paginacao) {
        var page = consultaService.listarConsultas(paginacao);
        return ResponseEntity.ok(page);
    }

}
