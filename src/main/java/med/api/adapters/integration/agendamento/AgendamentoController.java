package med.api.adapters.integration.agendamento;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestCreateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoRequestUpdateDTO;
import med.api.adapters.integration.agendamento.dto.AgendamentoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("api/agendamentos")
public class AgendamentoController {

    @Autowired
    AgendamentoService agendamentoService;

    @PostMapping
    @Transactional
    public void cadastrarAgendamento(@RequestBody @Valid AgendamentoRequestCreateDTO
                                                 agendamentoRequestCreateDTO) throws Exception {
        agendamentoService.cadastrarAgendamento(agendamentoRequestCreateDTO);
    }

    @GetMapping("/{id}")
    public AgendamentoResponseDTO obterAgendamentoPorId(@PathVariable Long id) {
        return agendamentoService.obterAgendamentoPorId(id);
    }

    @PutMapping ("/{id}")
    @Transactional
    public void atualizarAgendamentoPorId(@PathVariable Long id,
            @RequestBody @Valid AgendamentoRequestUpdateDTO agendamentoUpdateDTO) throws Exception {
        agendamentoService.alterarAgendamentoPorId(id, agendamentoUpdateDTO);
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public void deletarAgendamentoPorId(@PathVariable Long id) {
        agendamentoService.deletarAgendamentoPorId(id);
    }

    @GetMapping
    public Page<AgendamentoResponseDTO> obterListaDeAgendamentos(Pageable paginacao) {
        return agendamentoService.obterListaPaginadaDeAgendamentos(paginacao);
    }

}
