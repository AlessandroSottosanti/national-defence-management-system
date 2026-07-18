package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ArmedForceSeeder extends AbstractSeeder<ArmedForce> {

    private static final String[] NAMES = {
            "Army",
            "Navy",
            "Air Force",
            "Carabinieri",
            "Space Force",
            "Special Forces",
            "Logistics Command",
            "Cyber Command",
            "Coast Guard",
            "Military Police"
    };

    public ArmedForceSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<ArmedForce> getEntityClass() {
        return ArmedForce.class;
    }

    @Override
    protected ArmedForce createEntity(int index) {

        ArmedForce entity = new ArmedForce();

        entity.setName(
                NAMES[(index - 1) % NAMES.length]
        );

        return entity;
    }
}
