package ru.rostelecom.supply.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.rostelecom.supply.domain.AppUser;
import ru.rostelecom.supply.repository.AppUserRepository;

/** Общие атрибуты модели, нужные всем страницам (текущий раздел меню, пользователь). */
@ControllerAdvice
public class GlobalModelAdvice {

    private final AppUserRepository userRepository;

    public GlobalModelAdvice(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentUser")
    public AppUser currentUser(Authentication authentication) {
        if (authentication == null) return null;
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}
