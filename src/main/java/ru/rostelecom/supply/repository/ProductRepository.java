package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rostelecom.supply.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            select p from Product p
            where (:q = '' or lower(p.name) like lower(concat('%', :q, '%'))
                   or lower(p.sku) like lower(concat('%', :q, '%')))
              and (:categoryId is null or p.category.id = :categoryId)
            order by p.category.name, p.name
            """)
    List<Product> search(@Param("q") String q, @Param("categoryId") Long categoryId);

    List<Product> findAllByOrderByNameAsc();

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);
}
