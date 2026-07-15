package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentRepository extends JpaRepository<Documents, Integer>,
        JpaSpecificationExecutor<Documents> {
}
