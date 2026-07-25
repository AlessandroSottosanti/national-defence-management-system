package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Integer>, JpaSpecificationExecutor<Vehicle> {
}
