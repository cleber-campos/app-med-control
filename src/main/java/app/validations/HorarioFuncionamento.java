package app.validations;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.shared.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class HorarioFuncionamento implements ValicadaoConsulta{

    // Regra 1: Valida horário de funcionamento (segunda a sábado, 08:00 às 18:00)
    @Override
    public void validar(ConsultaRequestCreateDTO request) {
        var data = request.dataConsulta();

        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime horaConsulta = data.toLocalTime();
        if (diaDaSemana == DayOfWeek.SUNDAY ||
                horaConsulta.isBefore(LocalTime.of(8, 0)) ||
                horaConsulta.isAfter(LocalTime.of(18, 0))) {
            throw new ValidacaoException
                    ("A Consulta deve ser agendada de segunda e sábado, das 08:00 às 18:00.");
        }
    }
}
