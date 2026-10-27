package ru.rostelecom.supply.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Укажите наименование")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "Укажите ИНН")
    @Pattern(regexp = "\\d{10}|\\d{12}", message = "ИНН должен содержать 10 или 12 цифр")
    private String inn;

    @Pattern(regexp = "^$|\\d{9}", message = "КПП должен содержать 9 цифр")
    private String kpp;

    @Size(max = 150)
    @Column(name = "contact_person")
    private String contactPerson;

    @Size(max = 30)
    private String phone;

    @Email(message = "Некорректный e-mail")
    @Size(max = 150)
    private String email;

    @Size(max = 300)
    private String address;

    @NotNull
    @DecimalMin(value = "0.0", message = "Рейтинг от 0 до 5")
    @DecimalMax(value = "5.0", message = "Рейтинг от 0 до 5")
    private BigDecimal rating = new BigDecimal("5.0");

    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getInn() { return inn; }
    public void setInn(String inn) { this.inn = inn; }
    public String getKpp() { return kpp; }
    public void setKpp(String kpp) { this.kpp = kpp; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
