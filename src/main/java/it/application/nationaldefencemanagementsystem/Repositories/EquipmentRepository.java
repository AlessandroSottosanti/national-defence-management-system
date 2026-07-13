package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    List<Equipment> findByStatus(EquipmentStatus status);
    List<Equipment> findByCondition(EquipmentCondition condition);
    List<Equipment> findByFireArmTrue();

    //Trova armi da fuoco con munizioni sotto una certa soglia (es. per il rifornimento)
    List<Equipment> findByFireArmTrueAndAmmunitionCountLessThan(Integer threshold);

    //Ricerca combinata: Es. Trova equipaggiamenti ATTIVI ma USURATI
    List<Equipment> findByStatusAndCondition(EquipmentStatus status, EquipmentCondition condition);

    //Trova tutto l'equipaggiamento in dotazione a un soldato specifico
    List<Equipment> findByOperatorId(Integer operatorId);

    // Conta quanti equipaggiamenti ci sono in un determinato stato (es. quanti sono MANCANTI?)
    long countByStatus(EquipmentStatus status);

    // Conta quanti oggetti sono in riparazione
    long countByCondition(EquipmentCondition condition);

    // Trova tutti gli equipaggiamenti in magazzino non ancora assegnati a nessun operatore
    List<Equipment> findByOperatorIdIsNull();

    // Trova gli equipaggiamenti assegnati (che hanno un ID operatore valorizzato)
    List<Equipment> findByOperatorIdIsNotNull();

}
