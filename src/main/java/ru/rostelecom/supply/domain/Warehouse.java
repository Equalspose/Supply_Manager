package ru.rostelecom.supply.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Укажите код склада")
    @Size(max = 20)
    private String code;

    @NotBlank(message = "Укажите наименование")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Укажите макрорегион")
    @Size(max = 100)
    private String region;

    @NotBlank(message = "Укажите адрес")
    @Size(max = 300)
    private String address;

    @Size(max = 150)
    private String manager;

    @NotNull
    @Min(value = 1, message = "Вместимость должна быть больше нуля")
    private Integer capacity = 10000;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
