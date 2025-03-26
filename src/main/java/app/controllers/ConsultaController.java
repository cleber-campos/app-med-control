package app.controllers;

import app.dtos.PageDTO;
import app.dtos.consulta.ConsultaRequestCancelDTO;
import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.services.ConsultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping ("api/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> cadastrar(@RequestBody @Valid
    ConsultaRequestCreateDTO request, UriComponentsBuilder uriBuilder) {
        ConsultaResponseDTO consulta = consultaService.criarConsulta(request);
        var uri = uriBuilder.path("/consultas/{id}")
                .buildAndExpand(consulta.getId()).toUri();
        return ResponseEntity.created(uri).body(consulta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> consultar(@PathVariable Long id) {
        var consulta = consultaService.obterConsultaPorId(id);
        return ResponseEntity.ok(consulta);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ConsultaResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ConsultaRequestCreateDTO request) {
        var consulta = consultaService.atualizarConsulta(id, request);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id,
                                         ConsultaRequestCancelDTO request) {
        consultaService.cancelarConsulta(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<ConsultaResponseDTO>> listar(Pageable paginacao) {
        var page = consultaService.listarConsultas(paginacao);
        return ResponseEntity.ok(page);
    }

}
