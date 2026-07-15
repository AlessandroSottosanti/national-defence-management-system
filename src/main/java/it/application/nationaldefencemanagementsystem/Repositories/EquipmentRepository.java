package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EquipmentRepository extends JpaRepository<Equipment, Integer>,
        JpaSpecificationExecutor<Equipment>{

}
