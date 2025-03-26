package app.validations;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.repositories.MedicoRepository;
import app.shared.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DisponibilidadeMedico implements ValicadaoConsulta {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public void validar(ConsultaRequestCreateDTO request) {
        Long idMedico = request.idMedico();
        LocalDateTime dataConsulta = request.dataConsulta();

        if (idMedico != null) {
            if(!medicoRepository.existsById(idMedico)){
                throw new ValidacaoException("Id do medico nao existe");
            }
            if(medicoRepository.MedicoDisponivelNaData(idMedico, dataConsulta).isPresent()) {
                throw new ValidacaoException("O Medico ja possui um agendamento nesta data");
            }
        }
    }
}
