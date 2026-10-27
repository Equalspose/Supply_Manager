package ru.rostelecom.supply.domain;

/** Жизненный цикл заказа поставщику. */
public enum OrderStatus {
    DRAFT("Черновик", "gray"),
    APPROVED("Согласован", "blue"),
    SENT("Отправлен поставщику", "violet"),
    PARTIALLY_DELIVERED("Частично поставлен", "orange"),
    DELIVERED("Поставлен", "green"),
    CANCELLED("Отменён", "red");

    private final String title;
    private final String color;

    OrderStatus(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() { return title; }
    public String getColor() { return color; }

    public boolean isEditable() { return this == DRAFT; }
    public boolean canReceive() { return this == SENT || this == PARTIALLY_DELIVERED; }
    public boolean isActive() { return this != DELIVERED && this != CANCELLED; }
}
