package ru.rostelecom.supply.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rostelecom.supply.domain.OrderStatus;
import ru.rostelecom.supply.domain.PurchaseOrder;
import ru.rostelecom.supply.dto.DashboardStats;
import ru.rostelecom.supply.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final Locale RU = Locale.forLanguageTag("ru");

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final PurchaseOrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final StockService stockService;

    public DashboardService(SupplierRepository supplierRepository, ProductRepository productRepository,
                            WarehouseRepository warehouseRepository, PurchaseOrderRepository orderRepository,
                            DeliveryRepository deliveryRepository, StockService stockService) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.stockService = stockService;
    }

    public DashboardStats stats() {
        LocalDate today = LocalDate.now();
        List<OrderStatus> active = List.of(OrderStatus.DRAFT, OrderStatus.APPROVED, OrderStatus.SENT, OrderStatus.PARTIALLY_DELIVERED);

        long overdue = orderRepository.findByStatusInOrderByOrderDateAsc(active).stream()
                .filter(PurchaseOrder::isOverdue).count();

        // Объём закупок по месяцам за последние 12 месяцев (пустые месяцы = 0)
        Map<String, Object[]> byMonth = new HashMap<>();
        for (Object[] row : orderRepository.monthlyTotals()) {
            byMonth.put((String) row[0], row);
        }
        List<String> labels = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        YearMonth start = YearMonth.from(today).minusMonths(11);
        for (int i = 0; i < 12; i++) {
            YearMonth ym = start.plusMonths(i);
            Object[] row = byMonth.get(ym.toString());
            String month = ym.getMonth().getDisplayName(TextStyle.SHORT_STANDALONE, RU);
            labels.add(capitalize(month.replace(".", "")) + " " + String.valueOf(ym.getYear()).substring(2));
            amounts.add(row == null ? BigDecimal.ZERO : new BigDecimal(row[1].toString()));
            counts.add(row == null ? 0L : ((Number) row[2]).longValue());
        }

        Map<OrderStatus, Long> statusMap = new EnumMap<>(OrderStatus.class);
        for (Object[] row : orderRepository.countByStatusGrouped()) {
            statusMap.put((OrderStatus) row[0], (Long) row[1]);
        }
        List<String> statusLabels = new ArrayList<>();
        List<Long> statusCounts = new ArrayList<>();
        List<String> statusColors = new ArrayList<>();
        for (OrderStatus s : OrderStatus.values()) {
            statusLabels.add(s.getTitle());
            statusCounts.add(statusMap.getOrDefault(s, 0L));
            statusColors.add(s.getColor());
        }

        List<String> topNames = new ArrayList<>();
        List<BigDecimal> topAmounts = new ArrayList<>();
        for (Object[] row : orderRepository.topSuppliers(PageRequest.of(0, 5))) {
            topNames.add((String) row[0]);
            topAmounts.add((BigDecimal) row[1]);
        }

        return new DashboardStats(
                supplierRepository.countByActiveTrue(),
                productRepository.count(),
                warehouseRepository.count(),
                orderRepository.countByStatusIn(active),
                overdue,
                deliveryRepository.countByDeliveryDateGreaterThanEqual(today.withDayOfMonth(1)),
                orderRepository.sumAmountSince(today.withDayOfYear(1)),
                stockService.totalValue(),
                stockService.lowStock().size(),
                labels, amounts, counts,
                statusLabels, statusCounts, statusColors,
                topNames, topAmounts
        );
    }

    public List<PurchaseOrder> recentOrders() {
        return orderRepository.findTop6ByOrderByOrderDateDescIdDesc();
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
