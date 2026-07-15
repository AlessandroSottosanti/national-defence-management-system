package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public abstract class AbstractSeeder<T> {

    protected static final Logger log =
            LoggerFactory.getLogger(AbstractSeeder.class);

    protected static final int MIN_ROWS = 10;

    protected final EntityManager em;

    protected AbstractSeeder(EntityManager em) {
        this.em = em;
    }

    protected abstract Class<T> getEntityClass();

    protected abstract T createEntity(int index);

    protected String getSeederName() {
        return getClass().getSimpleName();
    }

    protected String skipReason() {
        return null;
    }

    public void seed() {

        String reason = skipReason();

        if(reason != null) {

            log.info(
                    "{} -> skipped ({})",
                    getSeederName(),
                    reason
            );

            return;
        }

        Long count = em.createQuery(
                "SELECT COUNT(e) FROM " +
                        getEntityClass().getSimpleName() +
                        " e",
                Long.class
        ).getSingleResult();

        if (count >= MIN_ROWS) {

            log.info(
                    "{} -> skipped ({} rows found)",
                    getSeederName(),
                    count
            );

            return;
        }

        int missingRows = MIN_ROWS - count.intValue();

        for (int i = 1; i <= missingRows; i++) {

            T entity = createEntity(
                    count.intValue() + i
            );

            em.persist(entity);
        }

        em.flush();

        log.info(
                "{} -> inserted {} rows",
                getSeederName(),
                missingRows
        );
    }
}