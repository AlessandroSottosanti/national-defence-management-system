package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    Optional<Operator> findByServiceNumber(String serviceNumber);
    List<Operator> findByStatus(OperatorStatus status);
    List<Operator> findByBaseId(Integer baseId);
    List<Operator> findByLastNameContainingIgnoreCase(String lastName);
}