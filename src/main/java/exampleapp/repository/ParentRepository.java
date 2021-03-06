package exampleapp.repository;

import exampleapp.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long>, JpaSpecificationExecutor<Parent> {
    Optional<Parent> findTopByOrderByIdDesc();
}
