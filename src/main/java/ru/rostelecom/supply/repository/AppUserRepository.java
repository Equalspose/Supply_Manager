package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rostelecom.supply.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
