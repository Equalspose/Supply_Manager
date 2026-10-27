package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rostelecom.supply.domain.Warehouse;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findAllByOrderByIdAsc();

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
