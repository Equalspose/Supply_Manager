package ru.rostelecom.supply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Система управления поставками предприятия (на примере ПАО «Ростелеком»).
 */
@SpringBootApplication
public class SupplyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplyManagementApplication.class, args);
    }
}
