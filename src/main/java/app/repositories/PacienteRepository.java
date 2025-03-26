package app.repositories;

import app.models.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findById(Long id);
    Page<Paciente> findAllByStatusTrue(Pageable paginacao);

    @Query("""
           SELECT p
           FROM Paciente p
           WHERE
           p.status = true AND
           p.id = :id AND
           p.id NOT IN (
                SELECT c.paciente.id
                FROM Consulta c
                WHERE c.paciente.id = :id AND
                c.dataConsulta = :data
           )
           """)
   Optional<Paciente> PacienteDisponivelNaData(Long id, LocalDateTime data);

}
