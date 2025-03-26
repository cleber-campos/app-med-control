package app.services;

import app.dtos.PageDTO;
import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.mappers.MedicoMapper;
import app.models.Medico;
import app.repositories.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class MedicoService {

    private static MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        MedicoService.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Transactional
    public MedicoResponseDTO criarMedico(MedicoRequestCreateDTO request) {
        Medico medico = medicoMapper.toEntity(request);
        var medicoCriado = medicoRepository.save(medico);
        return medicoMapper.toResponseDTO(medicoCriado);
    }

    public MedicoResponseDTO buscarMedico(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        return medicoMapper.toResponseDTO(medico);
    }

    public Medico obterMedicoPorId(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        return medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
    }

    @Transactional
    public MedicoResponseDTO atualizarMedico(Long id, MedicoRequestUpdateDTO request) {
        medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        var medicoAlterado = medicoMapper.updateFromDTO(request);
        medicoRepository.save(medicoAlterado);
        return medicoMapper.toResponseDTO(medicoAlterado);
    }

    @Transactional
    public void inativarMedico(Long id) {
        if (id == null) throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        if (!medico.getStatus()) throw new IllegalStateException("Medico já está inativo.");
        medico.setStatus(false);
        medicoRepository.save(medico);
    }

    public PageDTO<MedicoResponseDTO> listarMedicos(Pageable paginacao) {
        Page<Medico> medicos = medicoRepository.findAllByStatusTrue(paginacao);
        Page<MedicoResponseDTO> medicosPageDTO = medicos.map(medicoMapper::toResponseDTO);
        return new PageDTO<>(medicosPageDTO);
    }

    public Medico buscarMedicoAleatorio(LocalDateTime data){
        return medicoRepository.BuscarMedicoAleatorio(data)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
    }

}
