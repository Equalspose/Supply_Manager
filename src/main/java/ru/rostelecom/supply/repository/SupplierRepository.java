package ru.rostelecom.supply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rostelecom.supply.domain.Supplier;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
            select s from Supplier s
            where (:q = '' or lower(s.name) like lower(concat('%', :q, '%'))
                   or s.inn like concat('%', :q, '%')
                   or lower(s.contactPerson) like lower(concat('%', :q, '%')))
              and (:active is null or s.active = :active)
            order by s.name
            """)
    List<Supplier> search(@Param("q") String q, @Param("active") Boolean active);

    List<Supplier> findByActiveTrueOrderByNameAsc();

    boolean existsByInnAndIdNot(String inn, Long id);

    boolean existsByInn(String inn);

    long countByActiveTrue();
}
