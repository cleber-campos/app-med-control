package app.repositories;

import app.models.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findById(Long id);
    Page<Medico> findAllByStatusTrue(Pageable paginacao);

    @Query("""
            SELECT m FROM Medico m
            WHERE
            m.status = true AND
            m.id NOT IN (
                SELECT c.medico.id FROM Consulta c
                WHERE
                c.dataConsulta = :data
            )
            ORDER BY FUNCTION('random') LIMIT 1
            """)
    Optional<Medico> BuscarMedicoAleatorio(LocalDateTime data);

    @Query("""
            SELECT m FROM Medico m
            WHERE
            m.id = :id AND
            m.id NOT IN (
                SELECT c.medico.id FROM Consulta c
                WHERE
                c.dataConsulta = :data
            )
            """)
    Optional<Medico> MedicoDisponivelNaData(Long id, LocalDateTime data);

}
