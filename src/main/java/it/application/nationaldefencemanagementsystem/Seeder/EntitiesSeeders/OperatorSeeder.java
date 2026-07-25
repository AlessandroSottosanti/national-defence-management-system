package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class OperatorSeeder extends AbstractSeeder<Operator> {

    public OperatorSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Operator> getEntityClass() {
        return Operator.class;
    }

    @Override
    protected Operator createEntity(int index) {

        Base base =
                em.find(Base.class,
                        ((index - 1) % MIN_ROWS) + 1);

        Operator entity = new Operator();

        entity.setServiceNumber(
                "MIL-" + String.format("%04d", index)
        );

        entity.setFirstName(
                "Name" + index
        );

        entity.setLastName(
                "Surname" + index
        );

        entity.setRank(
                index % 2 == 0
                        ? "Captain"
                        : "Lieutenant"
        );

        entity.setHeightInCm(
                170 + (index % 20)
        );

        entity.setWeightInKg(
                70 + (index % 20)
        );

        entity.setStatus(
                OperatorStatus.values()
                        [index % OperatorStatus.values().length]
        );

        entity.setBase(base);

        return entity;
    }
}
