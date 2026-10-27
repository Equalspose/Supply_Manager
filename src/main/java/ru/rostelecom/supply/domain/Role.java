package ru.rostelecom.supply.domain;

public enum Role {
    ADMIN("Администратор"),
    MANAGER("Менеджер по закупкам"),
    STOREKEEPER("Кладовщик");

    private final String title;

    Role(String title) { this.title = title; }

    public String getTitle() { return title; }
}
