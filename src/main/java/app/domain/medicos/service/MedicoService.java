package app.domain.medicos.service;

import app.domain.medicos.dto.MedicoRequestCreateDTO;
import app.domain.medicos.dto.MedicoRequestUpdateDTO;
import app.domain.medicos.dto.MedicoResponseDTO;
import app.domain.medicos.model.Medico;
import app.domain.medicos.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    MedicoRepository medicoRepository;

    public MedicoResponseDTO cadastrarMedico(MedicoRequestCreateDTO medicoRequestCreateDTO) {
        var medico = new Medico(medicoRequestCreateDTO);
        medicoRepository.save(medico);
        return obterMedicoResponseDTOPorId(medico.getId());
    }

    public MedicoResponseDTO obterMedicoResponseDTOPorId(Long idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        }
        var medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        return new MedicoResponseDTO(medico.getId(), medico.getNome(), medico.getEmail(),
                medico.getCrm(), medico.getEspecialidade(), medico.getStatus());
    }


    public Medico obterMedicoPorId(Long idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        }
        return medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
    }

    public MedicoResponseDTO alterarMedicoPorId(Long idMedico, MedicoRequestUpdateDTO
            medicoRequestUpdateDTO) {
        var medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        medico.atualizaDadosMedico(medicoRequestUpdateDTO);
        return obterMedicoResponseDTOPorId(medico.getId());
    }

    public void inativarMedicoPorId(Long idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        }
        var medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
        medico.setStatus(false);
    }

    public Page<MedicoResponseDTO> obterListaPaginadaDeMedicosAtivos(Pageable paginacao) {
        return convertePageMedicoResponseDTO(medicoRepository.findAllByStatusTrue(paginacao));
    }

    public List<Medico> obterListaMedicosAtivos(){
        return medicoRepository.findAllByStatusTrue();
    }

    public Page<MedicoResponseDTO> convertePageMedicoResponseDTO(@PageableDefault(size = 10) Page<Medico> medicos) {
        return medicos.map(m -> new MedicoResponseDTO(m.getId(), m.getNome(), m.getEmail()
                , m.getCrm(), m.getEspecialidade(), m.getStatus()));
    }

}
