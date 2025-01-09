package app.domain.consultas.repository;

import app.domain.consultas.model.Consulta;
import app.domain.medicos.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findById(Long id);

    @Query("SELECT a FROM Consulta a " +
            "WHERE a.paciente.id = :idPaciente AND " +
            "DATE(a.dataConsulta) = DATE(:dataConsulta)")
    Optional<Consulta> findConsultaByPacienteAndData(@Param("idPaciente") Long idPaciente,
                                                     @Param("dataConsulta")LocalDateTime dataConsulta);

    @Query("SELECT a FROM Consulta a " +
            "WHERE a.medico.id = :idMedico AND " +
            "a.dataConsulta = :dataConsulta")
    Optional<Consulta> findConsultaByMedicoAndDataHora(@Param("idMedico") Long idMedico,
                                                       @Param("dataConsulta") LocalDateTime dataConsulta);

    @Query("SELECT a.medico FROM Consulta a " +
            "WHERE a.dataConsulta != :dataConsulta " +
            "ORDER BY FUNCTION ('RANDOM')")
    Page<Medico> findMedicoDisponivel(@Param("dataConsulta") LocalDateTime dataConsulta, Pageable limite);

}
