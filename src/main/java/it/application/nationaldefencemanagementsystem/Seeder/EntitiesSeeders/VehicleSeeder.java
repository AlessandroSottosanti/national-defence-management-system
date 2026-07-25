package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Entities.VehicleStatus;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("dev")
public class VehicleSeeder extends AbstractSeeder<Vehicle> {

    public VehicleSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Vehicle> getEntityClass() {
        return Vehicle.class;
    }

    @Override
    protected Vehicle createEntity(int index) {

        Vehicle vehicle = new Vehicle();

        vehicle.setMatricola(
                UUID.randomUUID()
        );

        vehicle.setModello(
                "Model-" + index
        );

        vehicle.setStato(
                VehicleStatus.values()[
                        index % VehicleStatus.values().length
                        ]
        );

        vehicle.setBase(
                em.find(Base.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        vehicle.setCategory(
                em.find(VehicleCategory.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        return vehicle;
    }
}