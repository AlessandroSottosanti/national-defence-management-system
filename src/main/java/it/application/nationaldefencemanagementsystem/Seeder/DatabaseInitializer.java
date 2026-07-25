package it.application.nationaldefencemanagementsystem.Seeder;

import it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(DatabaseInitializer.class);

    private final ArmedForceSeeder armedForceSeeder;
    private final BaseSeeder baseSeeder;
    private final VehicleCategorySeeder vehicleCategorySeeder;
    private final OperatorSeeder operatorSeeder;
    private final UserSeeder userSeeder;
    private final VehicleSeeder vehicleSeeder;
    private final EquipmentSeeder equipmentSeeder;
    private final MaintenanceSeeder maintenanceSeeder;
    private final DocumentsSeeder documentsSeeder;

    public DatabaseInitializer(
            ArmedForceSeeder armedForceSeeder,
            BaseSeeder baseSeeder,
            VehicleCategorySeeder vehicleCategorySeeder,
            OperatorSeeder operatorSeeder,
            UserSeeder userSeeder,
            VehicleSeeder vehicleSeeder,
            EquipmentSeeder equipmentSeeder,
            MaintenanceSeeder maintenanceSeeder,
            DocumentsSeeder documentsSeeder
    ) {
        this.armedForceSeeder = armedForceSeeder;
        this.baseSeeder = baseSeeder;
        this.vehicleCategorySeeder = vehicleCategorySeeder;
        this.operatorSeeder = operatorSeeder;
        this.userSeeder = userSeeder;
        this.vehicleSeeder = vehicleSeeder;
        this.equipmentSeeder = equipmentSeeder;
        this.maintenanceSeeder = maintenanceSeeder;
        this.documentsSeeder = documentsSeeder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        log.info("Starting database seeding...");

        armedForceSeeder.seed();
        baseSeeder.seed();

        vehicleCategorySeeder.seed();

        operatorSeeder.seed();
        userSeeder.seed();

        vehicleSeeder.seed();

        equipmentSeeder.seed();
        maintenanceSeeder.seed();
        documentsSeeder.seed();

        log.info("Database seeding completed.");
    }
}