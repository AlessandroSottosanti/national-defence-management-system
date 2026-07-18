package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArmedForceRepository extends JpaRepository<ArmedForce, Integer> {
    List<ArmedForce> findByNameContainingIgnoreCase(String name);
}