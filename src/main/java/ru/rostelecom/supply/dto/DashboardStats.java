package ru.rostelecom.supply.dto;

import java.math.BigDecimal;
import java.util.List;

/** Сводные показатели для главной страницы. */
public record DashboardStats(
        long activeSuppliers,
        long products,
        long warehouses,
        long activeOrders,
        long overdueOrders,
        long deliveriesThisMonth,
        BigDecimal spentThisYear,
        BigDecimal stockValue,
        int lowStockCount,
        List<String> monthLabels,
        List<BigDecimal> monthAmounts,
        List<Long> monthCounts,
        List<String> statusLabels,
        List<Long> statusCounts,
        List<String> statusColors,
        List<String> topSupplierNames,
        List<BigDecimal> topSupplierAmounts
) {}
