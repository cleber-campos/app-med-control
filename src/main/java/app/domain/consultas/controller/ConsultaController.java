package app.domain.consultas.controller;

import app.domain.consultas.dto.ConsultaRequestCreateDTO;
import app.domain.consultas.dto.ConsultaRequestUpdateDTO;
import app.domain.consultas.dto.ConsultaResponseDTO;
import app.domain.consultas.service.ConsultaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("api/consultas")
public class ConsultaController {

    @Autowired
    ConsultaService consultaService;

    @PostMapping
    @Transactional
    public void cadastrarConsulta(@RequestBody @Valid ConsultaRequestCreateDTO
                                                 ConsultaRequestCreateDTO) throws Exception {
        consultaService.cadastrarConsulta(ConsultaRequestCreateDTO);
    }

    @GetMapping("/{id}")
    public ConsultaResponseDTO obterConsultaPorId(@PathVariable Long id) {
        return consultaService.obterConsultaPorId(id);
    }

    @PutMapping ("/{id}")
    @Transactional
    public void atualizarConsulta(@PathVariable Long id,
            @RequestBody @Valid ConsultaRequestUpdateDTO ConsultaUpdateDTO) throws Exception {
        consultaService.atualizarConsulta(id, ConsultaUpdateDTO);
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public void cancelarConsulta(@PathVariable Long id) {
        consultaService.cancelarConsulta(id);
    }

    @GetMapping
    public Page<ConsultaResponseDTO> listarConsultas(Pageable paginacao) {
        return consultaService.listarConsultas(paginacao);
    }

}
