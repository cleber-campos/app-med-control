package app.domain.pacientes.service;

import app.domain.pacientes.dto.PacienteRequestCreatedDTO;
import app.domain.pacientes.dto.PacienteRequestUpdateDTO;
import app.domain.pacientes.dto.PacienteResponseDTO;
import app.domain.pacientes.model.Paciente;
import app.domain.pacientes.repository.PacienteRepository;
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

    public void alterarPaciente(Long idPaciente, PacienteRequestUpdateDTO pacienteUpdateDTO) {
        var paciente = pacienteRepository.getReferenceById(idPaciente);
        paciente.atualizaDadosPaciente(pacienteUpdateDTO);
    }

    public PacienteResponseDTO obterPacienteResponseDTOPorId(Long id) {
        return convertePacienteResponseDTO(pacienteRepository.findById(id));
    }

    public Paciente obterPacientePorId(Long id) {
        return pacienteRepository.getReferenceById(id);
    }

    public void InativarPaciente(Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.setStatus(false);
    }

    public Page<PacienteResponseDTO> obterListaDePacientes(Pageable paginacao) {
        return convertePagePacienteResponseDTO(pacienteRepository.findAllByStatusTrue(paginacao));
    }

    public Page<PacienteResponseDTO> convertePagePacienteResponseDTO(Page<Paciente> pacientes){
        return pacientes.map(p -> new PacienteResponseDTO(p.getId(), p.getNome(),
                p.getEmail(), p.getCpf(), p.getStatus()));
    }

    public PacienteResponseDTO convertePacienteResponseDTO(Optional<Paciente> paciente){
        if(paciente.isPresent()){
            var p = paciente.get();
            return new PacienteResponseDTO(p.getId(), p.getNome(), p.getEmail(),
                    p.getCpf(), p.getStatus());
        }
        return null;
    }

}
