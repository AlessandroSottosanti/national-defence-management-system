package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class EquipmentSeeder extends AbstractSeeder<Equipment> {

    public EquipmentSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Equipment> getEntityClass() {
        return Equipment.class;
    }

    @Override
    protected Equipment createEntity(int index) {

        Equipment entity = new Equipment();

        entity.setName("Equipment " + index);
        entity.setModel("Model " + index);

        entity.setCondition(
                EquipmentCondition.values()[
                        index % EquipmentCondition.values().length
                        ]
        );

        entity.setStatus(
                EquipmentStatus.values()[
                        index % EquipmentStatus.values().length
                        ]
        );

        entity.setFireArm(
                index % 2 == 0
        );

        entity.setAmmunitionCount(
                100 + index
        );

        entity.setOperator(
                em.find(Operator.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        return entity;
    }
}