package it.application.nationaldefencemanagementsystem.Seeder.EntitiesSeeders;

import it.application.nationaldefencemanagementsystem.Entities.Role;
import it.application.nationaldefencemanagementsystem.Entities.User;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class UserSeeder extends AbstractSeeder<User> {

    public UserSeeder(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected User createEntity(int index) {

        User user = new User();

        user.setUsername(
                "user" + index
        );

        user.setEmail(
                "user" + index + "@military.local"
        );

        user.setPassword(
                "password"
        );

        user.setRole(
                Role.values()[
                        index % Role.values().length
                        ]
        );

        user.setEnabled(true);

        return user;
    }
}
