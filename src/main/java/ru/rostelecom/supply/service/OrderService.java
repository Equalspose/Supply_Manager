package ru.rostelecom.supply.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.rostelecom.supply.domain.*;
import ru.rostelecom.supply.dto.OrderForm;
import ru.rostelecom.supply.dto.OrderItemForm;
import ru.rostelecom.supply.repository.PurchaseOrderRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class OrderService {

    public static final String NUMBER_PREFIX = "ЗП-";

    private final PurchaseOrderRepository repository;
    private final SupplierService supplierService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    public OrderService(PurchaseOrderRepository repository, SupplierService supplierService,
                        WarehouseService warehouseService, ProductService productService) {
        this.repository = repository;
        this.supplierService = supplierService;
        this.warehouseService = warehouseService;
        this.productService = productService;
    }

    public Page<PurchaseOrder> search(OrderStatus status, Long supplierId, Long warehouseId, String q, Pageable pageable) {
        return repository.search(status, supplierId, warehouseId, StringUtils.hasText(q) ? q.trim() : "", pageable);
    }

    public PurchaseOrder get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Заказ", id));
    }

    public List<PurchaseOrder> awaitingDelivery() {
        return repository.findByStatusInOrderByOrderDateAsc(List.of(OrderStatus.SENT, OrderStatus.PARTIALLY_DELIVERED));
    }

    public List<PurchaseOrder> bySupplier(Long supplierId) {
        return repository.findBySupplierIdOrderByOrderDateDesc(supplierId);
    }

    @Transactional
    public PurchaseOrder create(OrderForm form, String username) {
        PurchaseOrder order = new PurchaseOrder();
        order.setNumber(nextNumber());
        order.setCreatedBy(username);
        apply(order, form);
        return repository.save(order);
    }

    @Transactional
    public PurchaseOrder update(Long id, OrderForm form) {
        PurchaseOrder order = get(id);
        if (!order.getStatus().isEditable()) {
            throw new BusinessException("Редактировать можно только заказ в статусе «Черновик»");
        }
        order.getItems().clear();
        apply(order, form);
        return order;
    }

    public OrderForm toForm(PurchaseOrder order) {
        OrderForm form = new OrderForm();
        form.setSupplierId(order.getSupplier().getId());
        form.setWarehouseId(order.getWarehouse().getId());
        form.setExpectedDate(order.getExpectedDate());
        form.setComment(order.getComment());
        order.getItems().forEach(i -> form.getItems().add(
                new OrderItemForm(i.getProduct().getId(), i.getQuantity(), i.getUnitPrice())));
        return form;
    }

    @Transactional
    public void changeStatus(Long id, OrderStatus target) {
        PurchaseOrder order = get(id);
        OrderStatus current = order.getStatus();
        boolean allowed = switch (target) {
            case APPROVED -> current == OrderStatus.DRAFT;
            case SENT -> current == OrderStatus.APPROVED;
            case CANCELLED -> current == OrderStatus.DRAFT || current == OrderStatus.APPROVED || current == OrderStatus.SENT;
            case DRAFT -> current == OrderStatus.APPROVED;
            default -> false;
        };
        if (!allowed) {
            throw new BusinessException("Нельзя перевести заказ из статуса «" + current.getTitle()
                    + "» в статус «" + target.getTitle() + "»");
        }
        order.setStatus(target);
    }

    @Transactional
    public void delete(Long id) {
        PurchaseOrder order = get(id);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Удалить можно только черновик заказа");
        }
        repository.delete(order);
    }

    private void apply(PurchaseOrder order, OrderForm form) {
        Supplier supplier = supplierService.get(form.getSupplierId());
        if (!supplier.isActive() && order.getId() == null) {
            throw new BusinessException("Поставщик «" + supplier.getName() + "» неактивен");
        }
        order.setSupplier(supplier);
        order.setWarehouse(warehouseService.get(form.getWarehouseId()));
        order.setExpectedDate(form.getExpectedDate() != null ? form.getExpectedDate() : LocalDate.now().plusDays(14));
        order.setComment(form.getComment());

        Set<Long> seen = new HashSet<>();
        for (OrderItemForm line : form.getItems()) {
            if (!seen.add(line.getProductId())) {
                throw new BusinessException("Товар указан в заказе несколько раз — объедините строки");
            }
            Product product = productService.get(line.getProductId());
            order.addItem(new OrderItem(product, line.getQuantity(), line.getUnitPrice()));
        }
        order.recalculateTotal();
    }

    private String nextNumber() {
        String prefix = NUMBER_PREFIX + LocalDate.now().getYear() + "-";
        int next = repository.findTopByNumberStartingWithOrderByNumberDesc(prefix)
                .map(o -> Integer.parseInt(o.getNumber().substring(prefix.length())) + 1)
                .orElse(1);
        return prefix + String.format("%04d", next);
    }
}
