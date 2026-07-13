package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Integer> {
}
