package es.sm2baleares.base.repository;

import es.sm2baleares.base.model.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(long id);

    Optional<Task> findByName(String name);


    @Query(
            value = "SELECT * FROM Task t WHERE t.user = ?1",
            nativeQuery = true)
    List<Task> findAllTasksById(Long id);


    @Query(
            value = "SELECT * FROM Task t WHERE t.user = ?1 AND t.name = ?2",
            nativeQuery = true)
    Optional<Task> finTasksByIdAndName(Long id, String name);
}
