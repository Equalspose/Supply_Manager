package ru.rostelecom.supply.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rostelecom.supply.domain.AppUser;
import ru.rostelecom.supply.domain.Role;
import ru.rostelecom.supply.repository.AppUserRepository;

/** Создаёт демонстрационных пользователей при первом запуске. */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public DataInitializer(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.save(new AppUser("admin", encoder.encode("admin"), "Тульцев Артем ADMIN", Role.ADMIN));
        repository.save(new AppUser("manager", encoder.encode("manager"), "Тульцев Артем MANAGER", Role.MANAGER));
        repository.save(new AppUser("store", encoder.encode("store"), "Тульцев Артем STORE", Role.STOREKEEPER));
        log.info("Созданы демо-пользователи: admin/admin, manager/manager, store/store");
    }
}
