package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Documents;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DocumentsSeeder extends AbstractSeeder<Documents> {

    public DocumentsSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Documents> getEntityClass() {
        return Documents.class;
    }

    @Override
    protected Documents createEntity(int index) {

        Documents document = new Documents();

        document.setTitle(
                "Document " + index
        );

        document.setFilePath(
                "/documents/doc_" + index + ".pdf"
        );

        document.setOperator(
                em.find(Operator.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        document.setVehicle(
                em.find(Vehicle.class,
                        ((index - 1) % MIN_ROWS) + 1)
        );

        return document;
    }
}