package med.api.adapters.integration.paciente;

import med.api.adapters.integration.paciente.dto.PacienteRequestUpdateDTO;
import med.api.adapters.integration.paciente.dto.PacienteResponseDTO;
import med.api.adapters.integration.paciente.dto.PacienteResponseDTOAgend;
import med.api.adapters.repository.jpa.PacienteRepository;
import med.api.adapters.integration.paciente.dto.PacienteRequestCreatedDTO;
import med.api.domain.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    public void cadastrarPaciente(PacienteRequestCreatedDTO pacienteRequestCreatedDTO) {
        pacienteRepository.save(new Paciente(pacienteRequestCreatedDTO));
    }

    public void alterarPacientePorId(PacienteRequestUpdateDTO pacienteUpdateDTO) {
        var paciente = pacienteRepository.getReferenceById(pacienteUpdateDTO.id());
        paciente.atualizaDados(pacienteUpdateDTO);
    }

    public PacienteResponseDTO obterPacienteResponseDTOPorId(Long id) {
        return convertePacienteResponseDTO(pacienteRepository.findById(id));
    }

    public Paciente obterPacientePorId(Long id) {
        return pacienteRepository.getReferenceById(id);
    }

    public void InativarPacientePorId(Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.setAtivo(false);
    }

    public Page<PacienteResponseDTO> obterListaDePacientes(Pageable paginacao) {
        return convertePagePacienteResponseDTO(pacienteRepository.findAllByAtivoTrue(paginacao));
    }

    public Page<PacienteResponseDTO> convertePagePacienteResponseDTO(Page<Paciente> pacientes){
        return pacientes.map(p -> new PacienteResponseDTO(p.getId(), p.getNome(),
                p.getEmail(), p.getCpf(), p.isAtivo()));
    }

    public PacienteResponseDTO convertePacienteResponseDTO(Optional<Paciente> paciente){
        if(paciente.isPresent()){
            var p = paciente.get();
            return new PacienteResponseDTO(p.getId(), p.getNome(), p.getEmail(),
                    p.getCpf(), p.isAtivo());
        }
        return null;
    }

    public PacienteResponseDTOAgend convertePacienteResponseDTOAgend(Paciente paciente){
            return new PacienteResponseDTOAgend(
                    paciente.getId(),
                    paciente.getNome(),
                    paciente.getCpf());
    }


}
