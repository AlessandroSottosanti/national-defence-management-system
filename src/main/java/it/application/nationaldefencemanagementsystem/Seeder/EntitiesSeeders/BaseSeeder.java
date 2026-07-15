package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class BaseSeeder extends AbstractSeeder<Base> {

    public BaseSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Base> getEntityClass() {
        return Base.class;
    }

    @Override
    protected Base createEntity(int index) {

        ArmedForce armedForce =
                em.find(ArmedForce.class,
                        ((index - 1) % MIN_ROWS) + 1);

        Base entity = new Base();

        entity.setName("Base " + index);
        entity.setCity("City " + index);
        entity.setAddress("Military Street " + index);
        entity.setArmedForce(armedForce);

        return entity;
    }
}
