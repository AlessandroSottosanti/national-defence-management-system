package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArmedForceRepository extends JpaRepository<ArmedForce, Integer> {
    Optional<ArmedForce> findByName(String name);
}