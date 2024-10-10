package med.api.adapters.repository.jpa;

import med.api.domain.models.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findById(Long id);
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
}
