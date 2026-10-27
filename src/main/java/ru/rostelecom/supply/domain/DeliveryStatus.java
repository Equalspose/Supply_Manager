package ru.rostelecom.supply.domain;

public enum DeliveryStatus {
    ACCEPTED("Принята", "green"),
    ACCEPTED_WITH_ISSUES("Принята с замечаниями", "orange");

    private final String title;
    private final String color;

    DeliveryStatus(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() { return title; }
    public String getColor() { return color; }
}
