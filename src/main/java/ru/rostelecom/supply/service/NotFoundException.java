package ru.rostelecom.supply.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String what, Long id) {
        super(what + " с идентификатором " + id + " не найден(а)");
    }
}
