package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rostelecom.supply.domain.Product;
import ru.rostelecom.supply.domain.Stock;
import ru.rostelecom.supply.domain.Warehouse;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductAndWarehouse(Product product, Warehouse warehouse);

    @Query("""
            select s from Stock s
            where (:warehouseId is null or s.warehouse.id = :warehouseId)
              and (:categoryId is null or s.product.category.id = :categoryId)
              and (:q = '' or lower(s.product.name) like lower(concat('%', :q, '%'))
                   or lower(s.product.sku) like lower(concat('%', :q, '%')))
            order by s.warehouse.code, s.product.name
            """)
    List<Stock> search(@Param("warehouseId") Long warehouseId,
                       @Param("categoryId") Long categoryId,
                       @Param("q") String q);

    List<Stock> findByWarehouseIdOrderByProductNameAsc(Long warehouseId);

    @Query("select coalesce(sum(s.quantity), 0) from Stock s where s.warehouse.id = :warehouseId")
    long totalQuantityByWarehouse(@Param("warehouseId") Long warehouseId);

    @Query("select coalesce(sum(s.quantity * s.product.price), 0) from Stock s")
    java.math.BigDecimal totalStockValue();

    /** Товары, суммарный остаток которых по всем складам ниже минимального. [product, total] */
    @Query("""
            select p, coalesce(sum(s.quantity), 0) from Product p
            left join Stock s on s.product = p
            group by p
            having coalesce(sum(s.quantity), 0) < p.minStock
            order by coalesce(sum(s.quantity), 0) * 1.0 / p.minStock
            """)
    List<Object[]> findLowStock();
}
