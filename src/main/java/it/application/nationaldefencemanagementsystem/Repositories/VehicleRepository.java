package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
