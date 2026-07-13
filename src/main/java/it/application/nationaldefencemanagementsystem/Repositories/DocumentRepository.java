package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Documents, Integer> {
}
