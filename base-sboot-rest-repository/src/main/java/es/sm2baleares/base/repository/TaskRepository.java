package es.sm2baleares.base.repository;

import es.sm2baleares.base.model.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(long id);
}
