package app.validations;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.repositories.PacienteRepository;
import app.shared.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadePaciente implements ValicadaoConsulta{

    @Autowired
    private PacienteRepository pacienteRepository;

    // Regra 3: Valida se o paciente já possui um consulta
    // no mesmo dia;
    @Override
    public void validar(ConsultaRequestCreateDTO request) {
        if (!pacienteRepository.existsById(request.idPaciente())) {
            throw new ValidacaoException("O paciente esta inativo");
        }
        var paciente = pacienteRepository.PacienteDisponivelNaData(
                request.idPaciente(), request.dataConsulta());
        if (paciente.isEmpty()){
            throw new ValidacaoException
                    ("O paciente ja possui um agendamento nesta data");
        }
    }
}
