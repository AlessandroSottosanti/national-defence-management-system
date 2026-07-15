package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class VehicleCategorySeeder extends AbstractSeeder<VehicleCategory> {

    private static final String[] NAMES = {
            "Tank",
            "APC",
            "Truck",
            "Helicopter",
            "Jet",
            "Drone",
            "Ship",
            "Submarine",
            "Artillery",
            "Utility Vehicle"
    };

    public VehicleCategorySeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<VehicleCategory> getEntityClass() {
        return VehicleCategory.class;
    }

    @Override
    protected VehicleCategory createEntity(int index) {

        VehicleCategory category = new VehicleCategory();

        category.setName(
                NAMES[(index - 1) % NAMES.length]
        );

        return category;
    }
}