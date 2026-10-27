package ru.rostelecom.supply.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.rostelecom.supply.domain.*;
import ru.rostelecom.supply.dto.DeliveryForm;
import ru.rostelecom.supply.repository.DeliveryRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DeliveryService {

    public static final String NUMBER_PREFIX = "ПТ-";

    private final DeliveryRepository repository;
    private final OrderService orderService;
    private final StockService stockService;

    public DeliveryService(DeliveryRepository repository, OrderService orderService, StockService stockService) {
        this.repository = repository;
        this.orderService = orderService;
        this.stockService = stockService;
    }

    public Page<Delivery> search(String q, Pageable pageable) {
        return repository.search(StringUtils.hasText(q) ? q.trim() : "", pageable);
    }

    public Delivery get(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Поставка", id));
    }

    /** Заготовка формы приёмки: по умолчанию принимаем весь остаток по каждой позиции. */
    public DeliveryForm prepareForm(PurchaseOrder order) {
        DeliveryForm form = new DeliveryForm();
        form.setOrderId(order.getId());
        order.getItems().forEach(i -> form.getLines().add(new DeliveryForm.Line(i.getId(), i.getRemaining())));
        return form;
    }

    /**
     * Приёмка поставки: увеличивает принятое количество по позициям заказа,
     * оприходует товар на склад заказа и пересчитывает статус заказа.
     */
    @Transactional
    public Delivery receive(DeliveryForm form, String username) {
        PurchaseOrder order = orderService.get(form.getOrderId());
        if (!order.getStatus().canReceive()) {
            throw new BusinessException("Принять поставку можно только по заказу, отправленному поставщику");
        }
        if (form.getDeliveryDate().isAfter(LocalDate.now())) {
            throw new BusinessException("Дата поставки не может быть в будущем");
        }

        Map<Long, OrderItem> items = order.getItems().stream()
                .collect(Collectors.toMap(OrderItem::getId, Function.identity()));

        Delivery delivery = new Delivery();
        delivery.setNumber(nextNumber());
        delivery.setOrder(order);
        delivery.setDeliveryDate(form.getDeliveryDate());
        delivery.setWaybill(form.getWaybill());
        delivery.setCarrier(form.getCarrier());
        delivery.setStatus(form.getStatus());
        delivery.setComment(form.getComment());
        delivery.setReceivedBy(username);

        for (DeliveryForm.Line line : form.getLines()) {
            int qty = line.getQuantity() == null ? 0 : line.getQuantity();
            if (qty == 0) continue;
            OrderItem item = items.get(line.getOrderItemId());
            if (item == null) {
                throw new BusinessException("Позиция не относится к заказу " + order.getNumber());
            }
            if (qty < 0 || qty > item.getRemaining()) {
                throw new BusinessException("По позиции «" + item.getProduct().getName()
                        + "» можно принять от 0 до " + item.getRemaining() + " " + item.getProduct().getUnit());
            }
            item.setReceivedQty(item.getReceivedQty() + qty);
            delivery.addItem(new DeliveryItem(item, qty));
            stockService.receive(item.getProduct(), order.getWarehouse(), qty);
        }

        if (delivery.getItems().isEmpty()) {
            throw new BusinessException("Укажите принятое количество хотя бы по одной позиции");
        }

        boolean complete = order.getItems().stream().allMatch(i -> i.getRemaining() == 0);
        order.setStatus(complete ? OrderStatus.DELIVERED : OrderStatus.PARTIALLY_DELIVERED);
        return repository.save(delivery);
    }

    private String nextNumber() {
        String prefix = NUMBER_PREFIX + LocalDate.now().getYear() + "-";
        int next = repository.findTopByNumberStartingWithOrderByNumberDesc(prefix)
                .map(d -> Integer.parseInt(d.getNumber().substring(prefix.length())) + 1)
                .orElse(1);
        return prefix + String.format("%04d", next);
    }
}
