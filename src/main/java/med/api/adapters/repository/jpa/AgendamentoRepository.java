package med.api.adapters.repository.jpa;

import med.api.domain.models.Agendamento;
import med.api.domain.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    Optional<Agendamento> findById(Long id);

    @Query("SELECT a FROM Agendamento a " +
            "WHERE a.paciente.id = :idPaciente AND " +
            "DATE(a.dataHora) = DATE(:dataHora)")
    Optional<Agendamento> findAgendamentoByPacienteAndData(@Param("idPaciente") Long idPaciente,
                                                           @Param("dataHora")LocalDateTime dataHora);

    @Query("SELECT a FROM Agendamento a " +
            "WHERE a.medico.id = :idMedico AND " +
            "a.dataHora = :dataHora")
    Optional<Agendamento> findAgendamentoByMedicoAndDataHora(@Param("idMedico") Long idMedico,
                                                             @Param("dataHora")LocalDateTime dataHora);

    //List<Agendamento> buscarAgendamentoPorMedico(Long idMedico);

    //List<Agendamento> buscarAgendamentoPorPaciente(Long idPaciente);

}
