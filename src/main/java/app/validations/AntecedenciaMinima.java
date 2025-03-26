package app.validations;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.shared.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AntecedenciaMinima implements ValicadaoConsulta{

    // Regra 2: Valida a antecedência mínima de 30 minutos
    @Override
    public void validar(ConsultaRequestCreateDTO request) {
        var data = request.dataConsulta();
        LocalDateTime agora = LocalDateTime.now();

        Duration diferencaTempo = Duration.between(agora, data);
        if (diferencaTempo.toMinutes() < 30) throw new ValidacaoException
                ("A Consulta deve ser agendada com, no mínimo, 30 minutos de antecedência.");
    }


}
