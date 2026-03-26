package com.taurupro.marketplace;

import com.taurupro.marketplace.domain.dto.SignUpAdmin;
import com.taurupro.marketplace.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarketInitializer implements ApplicationRunner {

    private final UserService userService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.lastName}")
    private String adminLastName;

    @Value("${app.admin.password}")
    private String adminPassword;

    public MarketInitializer(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean adminExists = userService.findByEmail(adminEmail).isPresent();

        if (adminExists) {
            log.info("Admin user already exists, skipping creation.");
            return;
        }

        log.info("Creating system admin user...");
        userService.createAdminUser(new SignUpAdmin(adminEmail, adminName, adminLastName, adminPassword));
        log.info("Admin user created successfully.");
    }
}
