package ru.rostelecom.supply.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rostelecom.supply.domain.Delivery;

import java.time.LocalDate;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("""
            select d from Delivery d
            where (:q = '' or lower(d.number) like lower(concat('%', :q, '%'))
                   or lower(d.order.number) like lower(concat('%', :q, '%'))
                   or lower(d.order.supplier.name) like lower(concat('%', :q, '%'))
                   or lower(d.waybill) like lower(concat('%', :q, '%')))
            """)
    Page<Delivery> search(@Param("q") String q, Pageable pageable);

    Optional<Delivery> findTopByNumberStartingWithOrderByNumberDesc(String prefix);

    long countByDeliveryDateGreaterThanEqual(LocalDate from);
}
