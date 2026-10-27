package ru.rostelecom.supply.service;

/** Ошибка бизнес-логики, текст которой показывается пользователю. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
