package ru.rostelecom.supply.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Укажите артикул")
    @Size(max = 40)
    private String sku;

    @NotBlank(message = "Укажите наименование")
    @Size(max = 200)
    private String name;

    @NotNull(message = "Выберите категорию")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotBlank
    @Size(max = 20)
    private String unit = "шт";

    @NotNull(message = "Укажите цену")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Минимальный остаток не может быть отрицательным")
    @Column(name = "min_stock")
    private Integer minStock = 0;

    @Size(max = 500)
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer minStock) { this.minStock = minStock; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
