package app.domain.medicos.repository;

import app.domain.medicos.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findById(Long id);
    Page<Medico> findAllByStatusTrue(Pageable paginacao);
    List<Medico> findAllByStatusTrue();
}
