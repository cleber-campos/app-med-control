package med.api.adapters.integration.medico;

import med.api.adapters.integration.medico.dto.MedicoRequestCreateDTO;
import med.api.adapters.integration.medico.dto.MedicoRequestUpdateDTO;
import med.api.adapters.integration.medico.dto.MedicoResponseDTO;
import med.api.adapters.repository.jpa.MedicoRepository;
import med.api.domain.models.Medico;
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

    public void cadastrarMedico(MedicoRequestCreateDTO medicoRequestCreateDTO) {
        medicoRepository.save(new Medico(medicoRequestCreateDTO));
    }

    public MedicoResponseDTO obterMedicoPorId(Long idMedico) {
        return converteMedicoResponseDTO(medicoRepository.findById(idMedico));
    }

    public void alterarMedicoPorId(MedicoRequestUpdateDTO medicoRequestUpdateDTO) {
        var medico = medicoRepository.getReferenceById(medicoRequestUpdateDTO.id());
        medico.atualizaDadosMedico(medicoRequestUpdateDTO);
    }

    public void inativarMedicoPorId(Long idMedico) {
        //medicoRepository.deleteById(idMedico);
        var medico = medicoRepository.getReferenceById(idMedico);
        medico.inativarMedico();
    }

    public Page<MedicoResponseDTO> obterListaPaginadaDeMedicosAtivos(Pageable paginacao) {
        return convertePageMedicoResponseDTO(medicoRepository.findAllByAtivoTrue(paginacao));
    }

    public Page<MedicoResponseDTO> convertePageMedicoResponseDTO(@PageableDefault (size = 10) Page<Medico> medicos){
        return medicos.map(m -> new MedicoResponseDTO(m.getId(), m.getNome(), m.getEmail()
                        , m.getCrm(), m.getEspecialidade(), m.isAtivo()));
    }

    public MedicoResponseDTO converteMedicoResponseDTO(Optional<Medico> medico){
        if(medico.isPresent()){
            var m = medico.get();
            return new MedicoResponseDTO(m.getId(), m.getNome(), m.getEmail(),
                    m.getCrm(), m.getEspecialidade(), m.isAtivo());
        }
        return null;
    }
}
