package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Maintenance;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Profile("dev")
public class MaintenanceSeeder extends AbstractSeeder<Maintenance> {

    public MaintenanceSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Maintenance> getEntityClass() {
        return Maintenance.class;
    }

    @Override
    protected Maintenance createEntity(int index) {

        Maintenance entity = new Maintenance();

        entity.setVehicle(
                em.find(Vehicle.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        entity.setDescription(
                "Routine maintenance " + index
        );

        entity.setStartDate(
                LocalDate.now().minusDays(index)
        );

        entity.setEndDate(
                LocalDate.now().plusDays(index)
        );

        entity.setEstimatedMaintenanceDays(
                index
        );

        entity.setCost(
                BigDecimal.valueOf(
                        500 + index * 100
                )
        );

        return entity;
    }
}
