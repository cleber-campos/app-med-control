package app.domain.pacientes.service;

import app.domain.pacientes.dto.PacienteRequestCreateDTO;
import app.domain.pacientes.dto.PacienteRequestUpdateDTO;
import app.domain.pacientes.dto.PacienteResponseDTO;
import app.domain.pacientes.model.Paciente;
import app.domain.pacientes.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    public PacienteResponseDTO cadastrarPaciente(PacienteRequestCreateDTO pacienteRequestCreateDTO) {
        var paciente = new Paciente(pacienteRequestCreateDTO);
        pacienteRepository.save(paciente);
        return obterPacienteResponseDTOPorId(paciente.getId());
    }

    public PacienteResponseDTO alterarPaciente(Long idPaciente, PacienteRequestUpdateDTO pacienteUpdateDTO) {
        var paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        paciente.atualizaDadosPaciente(pacienteUpdateDTO);
        return obterPacienteResponseDTOPorId(paciente.getId());
    }

    public PacienteResponseDTO obterPacienteResponseDTOPorId(Long idPaciente) {
        if (idPaciente == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        var paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        return convertePacienteResponseDTO(paciente);
    }

    public Paciente obterPacientePorId(Long idPaciente) {
        if (idPaciente == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        return pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
    }

    public void InativarPaciente(Long idPaciente) {
        if (idPaciente == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        var paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        paciente.setStatus(false);
        pacienteRepository.save(paciente);
    }

    public Page<PacienteResponseDTO> obterListaDePacientes(Pageable paginacao) {
        return convertePagePacienteResponseDTO(pacienteRepository.findAllByStatusTrue(paginacao));
    }

    public Page<PacienteResponseDTO> convertePagePacienteResponseDTO(Page<Paciente> pacientes){
        return pacientes.map(p -> new PacienteResponseDTO(
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getCpf(),
                p.getStatus()));
    }

    public PacienteResponseDTO convertePacienteResponseDTO(Paciente paciente){
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf(),
                paciente.getStatus());
    }

}
