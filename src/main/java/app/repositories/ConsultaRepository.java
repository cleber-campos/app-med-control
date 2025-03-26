package app.repositories;

import app.models.Consulta;
import app.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findById(Long id);

    @Query("SELECT c FROM Consulta c " +
            "WHERE c.paciente.id = :idPaciente AND " +
            "DATE(c.dataConsulta) = DATE(:dataConsulta)")
    Optional<Consulta> buscarPacienteNaData(@Param("idPaciente") Long idPaciente,
                                            @Param("dataConsulta")LocalDateTime dataConsulta);

    @Query("SELECT c FROM Consulta c JOIN FETCH c.medico " +
            "WHERE c.medico.id = :idMedico AND " +
            "c.dataConsulta = :dataConsulta")
    Optional<Consulta> buscarMedicoNaData(@Param("idMedico") Long id,
                                          @Param("dataConsulta") LocalDateTime data);
}
