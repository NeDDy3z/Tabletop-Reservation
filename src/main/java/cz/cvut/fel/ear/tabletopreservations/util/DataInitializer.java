package cz.cvut.fel.ear.tabletopreservations.util;

import cz.cvut.fel.ear.tabletopreservations.model.Role;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import cz.cvut.fel.ear.tabletopreservations.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        createAdminUser();
    }

    private void createAdminUser() {
        // Check if admin user already exists
        User existingAdmin = userService.findByEmail("admin@test.com");
        if (existingAdmin != null) {
            LOG.info("Admin user already exists, skipping creation.");
            return;
        }

        // TODO: Default admin credentials, to be removed.
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin");
        admin.setRoles(List.of(new Role[]{Role.ADMIN, Role.PLAYER, Role.PROVIDER}));
        admin.setActive(true);

        userService.addNewUser(admin);
        LOG.info("Admin user created with email: admin@test.com and password: admin");
    }
}

