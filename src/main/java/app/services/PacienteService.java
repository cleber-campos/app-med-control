package app.services;

import app.dtos.PageDTO;
import app.dtos.pacientes.PacienteRequestCreateDTO;
import app.dtos.pacientes.PacienteRequestUpdateDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import app.mappers.PacienteMapper;
import app.models.Paciente;
import app.repositories.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository,
                           PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Transactional
    public PacienteResponseDTO criarPaciente(PacienteRequestCreateDTO request) {
        Paciente paciente = pacienteMapper.toEntity(request);
        Paciente pacienteCriado = pacienteRepository.save(paciente);
        return pacienteMapper.toResponse(pacienteCriado);
    }

    @Transactional
    public PacienteResponseDTO atualizarPaciente(Long id, PacienteRequestUpdateDTO request) {
        if(id == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
       pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        Paciente pacienteAlterado = pacienteMapper.updateFromDTO(request);
        pacienteRepository.save(pacienteAlterado);
        return pacienteMapper.toResponse(pacienteAlterado);
    }

    public PacienteResponseDTO buscarPaciente(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        return pacienteMapper.toResponse(paciente);
    }

    public Paciente obterPacientePorId(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
    }

    public void inativarPaciente(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do paciente não pode ser nulo.");
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        if (!paciente.getStatus()) throw new IllegalStateException("Paciente já está inativo.");
        paciente.setStatus(false);
        pacienteRepository.save(paciente);
    }

    public PageDTO<PacienteResponseDTO> listarPacientes(Pageable page) {
        Page<Paciente> pacientes = pacienteRepository.findAllByStatusTrue(page);
        Page<PacienteResponseDTO> pacientesPageDTO = pacientes.map(pacienteMapper::toResponse);
        return new PageDTO<>(pacientesPageDTO);
    }
}
