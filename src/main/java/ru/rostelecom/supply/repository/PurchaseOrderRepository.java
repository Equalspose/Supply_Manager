package ru.rostelecom.supply.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rostelecom.supply.domain.OrderStatus;
import ru.rostelecom.supply.domain.PurchaseOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
            select o from PurchaseOrder o
            where (:status is null or o.status = :status)
              and (:supplierId is null or o.supplier.id = :supplierId)
              and (:warehouseId is null or o.warehouse.id = :warehouseId)
              and (:q = '' or lower(o.number) like lower(concat('%', :q, '%'))
                   or lower(o.supplier.name) like lower(concat('%', :q, '%')))
            """)
    Page<PurchaseOrder> search(@Param("status") OrderStatus status,
                               @Param("supplierId") Long supplierId,
                               @Param("warehouseId") Long warehouseId,
                               @Param("q") String q,
                               Pageable pageable);

    List<PurchaseOrder> findTop6ByOrderByOrderDateDescIdDesc();

    List<PurchaseOrder> findBySupplierIdOrderByOrderDateDesc(Long supplierId);

    List<PurchaseOrder> findByStatusInOrderByOrderDateAsc(List<OrderStatus> statuses);

    long countByStatus(OrderStatus status);

    long countByStatusIn(List<OrderStatus> statuses);

    Optional<PurchaseOrder> findTopByNumberStartingWithOrderByNumberDesc(String prefix);

    @Query("select coalesce(sum(o.totalAmount), 0) from PurchaseOrder o where o.status <> 'CANCELLED' and o.orderDate >= :from")
    BigDecimal sumAmountSince(@Param("from") LocalDate from);

    @Query("select coalesce(sum(o.totalAmount), 0) from PurchaseOrder o where o.supplier.id = :supplierId and o.status <> 'CANCELLED'")
    BigDecimal sumAmountBySupplier(@Param("supplierId") Long supplierId);

    @Query(value = """
            select to_char(date_trunc('month', order_date), 'YYYY-MM') as month,
                   sum(total_amount) as amount,
                   count(*) as cnt
            from purchase_order
            where status <> 'CANCELLED'
              and order_date >= date_trunc('month', current_date) - interval '11 months'
            group by 1
            order by 1
            """, nativeQuery = true)
    List<Object[]> monthlyTotals();

    @Query("select o.status, count(o) from PurchaseOrder o group by o.status")
    List<Object[]> countByStatusGrouped();

    @Query("""
            select o.supplier.name, sum(o.totalAmount) from PurchaseOrder o
            where o.status <> 'CANCELLED'
            group by o.supplier.name
            order by sum(o.totalAmount) desc
            """)
    List<Object[]> topSuppliers(Pageable pageable);
}
