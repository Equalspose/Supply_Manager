package ru.rostelecom.supply.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.rostelecom.supply.repository.AppUserRepository;

/**
 * Разграничение доступа по ролям:
 * ADMIN — всё; MANAGER — поставщики, товары, заказы; STOREKEEPER — приёмка поставок.
 * Просмотр доступен всем авторизованным пользователям.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/vendor/**", "/favicon.ico", "/login", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/warehouses/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/suppliers/**", "/products/**", "/orders/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/deliveries/**").hasAnyRole("ADMIN", "STOREKEEPER")
                .requestMatchers("/warehouses/new", "/warehouses/*/edit").hasRole("ADMIN")
                .requestMatchers("/suppliers/new", "/suppliers/*/edit", "/products/new", "/products/*/edit",
                                 "/orders/new", "/orders/*/edit").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/deliveries/new").hasAnyRole("ADMIN", "STOREKEEPER")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository repository) {
        return username -> repository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole().name())
                        .disabled(!u.isEnabled())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
}
