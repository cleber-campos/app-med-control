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
import java.util.Optional;

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
        return converteMedicoResponseDTO(medicoRepository.findById(idMedico));
    }

    public Medico obterMedicoPorId(Long idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        }
        return medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));
    }

    public MedicoResponseDTO alterarMedicoPorId(Long idMedico, MedicoRequestUpdateDTO medicoRequestUpdateDTO) {
        var medico = medicoRepository.getReferenceById(idMedico);
        medico.atualizaDadosMedico(medicoRequestUpdateDTO);
        return obterMedicoResponseDTOPorId(medico.getId());
    }

    public void inativarMedicoPorId(Long idMedico) {
        //medicoRepository.deleteById(idMedico);
        if (idMedico == null) {
            throw new IllegalArgumentException("O ID do médico não pode ser nulo.");
        }
        var medico = medicoRepository.getReferenceById(idMedico);
        medico.setStatus(false);
    }

    public Page<MedicoResponseDTO> obterListaPaginadaDeMedicosAtivos(Pageable paginacao) {
        return convertePageMedicoResponseDTO(medicoRepository.findAllByStatusTrue(paginacao));
    }

    public Page<MedicoResponseDTO> convertePageMedicoResponseDTO(@PageableDefault(size = 10) Page<Medico> medicos) {
        return medicos.map(m -> new MedicoResponseDTO(m.getId(), m.getNome(), m.getEmail()
                , m.getCrm(), m.getEspecialidade(), m.getStatus()));
    }

    public MedicoResponseDTO converteMedicoResponseDTO(Optional<Medico> medico) {
        if (medico.isPresent()) {
            var m = medico.get();
            return new MedicoResponseDTO(m.getId(), m.getNome(), m.getEmail(),
                    m.getCrm(), m.getEspecialidade(), m.getStatus());
        }
        return null;
    }

}
