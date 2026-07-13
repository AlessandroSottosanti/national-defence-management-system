package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}
