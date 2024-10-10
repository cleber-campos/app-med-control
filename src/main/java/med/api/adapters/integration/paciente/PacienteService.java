package med.api.adapters.integration.paciente;

import med.api.adapters.integration.paciente.dto.PacienteRequestUpdateDTO;
import med.api.adapters.integration.paciente.dto.PacienteResponseDTO;
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

    public PacienteResponseDTO obterPacientePorId(Long idMedico) {
        return convertePacienteResponseDTO(pacienteRepository.findById(idMedico));
    }

    public void alterarPacientePorId(PacienteRequestUpdateDTO pacienteUpdateDTO) {
        var paciente = pacienteRepository.getReferenceById(pacienteUpdateDTO.id());
        paciente.atualizaDados(pacienteUpdateDTO);
    }

    public void InativarPacientePorId(Long idPaciente) {
        var paciente = pacienteRepository.getReferenceById(idPaciente);
        paciente.inativarPaciente();
    }

    public Page<PacienteResponseDTO> obterListaDePacientes(Pageable paginacao) {
        return convertePagePacienteResponseDTO(pacienteRepository.findAllByAtivoTrue(paginacao));
    }

    public Page<PacienteResponseDTO> convertePagePacienteResponseDTO(Page<Paciente> pacientes){
        return pacientes.map(p -> new PacienteResponseDTO(p.getId(), p.getNome(),
                p.getEmail(), p.getCpf(), p.getAtivo()));
    }

    public PacienteResponseDTO convertePacienteResponseDTO(Optional<Paciente> paciente){
        if(paciente.isPresent()){
            var p = paciente.get();
            return new PacienteResponseDTO(p.getId(), p.getNome(), p.getEmail(), p.getCpf(), p.getAtivo());
        }
        return null;
    }


}
