package ru.rostelecom.supply.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rostelecom.supply.domain.OrderStatus;
import ru.rostelecom.supply.domain.PurchaseOrder;
import ru.rostelecom.supply.dto.OrderForm;
import ru.rostelecom.supply.dto.OrderItemForm;
import ru.rostelecom.supply.service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final OrderService orderService;
    private final SupplierService supplierService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    public OrderController(OrderService orderService, SupplierService supplierService,
                           WarehouseService warehouseService, ProductService productService) {
        this.orderService = orderService;
        this.supplierService = supplierService;
        this.warehouseService = warehouseService;
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) OrderStatus status,
                       @RequestParam(required = false) Long supplierId,
                       @RequestParam(required = false) Long warehouseId,
                       @RequestParam(required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(0, page), 15, Sort.by("orderDate").descending().and(Sort.by("id").descending()));
        Page<PurchaseOrder> orders = orderService.search(status, supplierId, warehouseId, q, pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("suppliers", supplierService.search(null, null));
        model.addAttribute("warehouses", warehouseService.findAll());
        model.addAttribute("status", status);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("warehouseId", warehouseId);
        model.addAttribute("q", q);
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.get(id));
        return "orders/view";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long productId,
                             @RequestParam(required = false) Integer qty,
                             Model model) {
        OrderForm form = new OrderForm();
        if (productId != null) {
            var product = productService.get(productId);
            form.getItems().add(new OrderItemForm(productId, qty != null ? qty : product.getMinStock(), product.getPrice()));
        } else {
            form.getItems().add(new OrderItemForm());
        }
        return showForm(form, null, model);
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        PurchaseOrder order = orderService.get(id);
        if (!order.getStatus().isEditable()) {
            ra.addFlashAttribute("error", "Редактировать можно только черновик");
            return "redirect:/orders/" + id;
        }
        return showForm(orderService.toForm(order), order, model);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") OrderForm form, BindingResult result,
                         Model model, Authentication auth, RedirectAttributes ra) {
        form.getItems().removeIf(i -> i.getProductId() == null && i.getQuantity() == null);
        if (result.hasErrors() || form.getItems().isEmpty()) {
            if (form.getItems().isEmpty()) model.addAttribute("error", "Добавьте хотя бы одну позицию");
            return showForm(form, null, model);
        }
        try {
            PurchaseOrder order = orderService.create(form, auth.getName());
            ra.addFlashAttribute("success", "Заказ " + order.getNumber() + " создан");
            return "redirect:/orders/" + order.getId();
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return showForm(form, null, model);
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") OrderForm form, BindingResult result,
                         Model model, RedirectAttributes ra) {
        form.getItems().removeIf(i -> i.getProductId() == null && i.getQuantity() == null);
        if (result.hasErrors() || form.getItems().isEmpty()) {
            if (form.getItems().isEmpty()) model.addAttribute("error", "Добавьте хотя бы одну позицию");
            return showForm(form, orderService.get(id), model);
        }
        try {
            PurchaseOrder order = orderService.update(id, form);
            ra.addFlashAttribute("success", "Заказ " + order.getNumber() + " обновлён");
            return "redirect:/orders/" + id;
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return showForm(form, orderService.get(id), model);
        }
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam OrderStatus target, RedirectAttributes ra) {
        try {
            orderService.changeStatus(id, target);
            ra.addFlashAttribute("success", "Статус заказа изменён на «" + target.getTitle() + "»");
        } catch (BusinessException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            orderService.delete(id);
            ra.addFlashAttribute("success", "Черновик заказа удалён");
            return "redirect:/orders";
        } catch (BusinessException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/" + id;
        }
    }

    /** Выгрузка реестра заказов в CSV (открывается в Excel). */
    @GetMapping("/export")
    public void export(@RequestParam(required = false) OrderStatus status,
                       @RequestParam(required = false) Long supplierId,
                       @RequestParam(required = false) Long warehouseId,
                       @RequestParam(required = false) String q,
                       HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"orders.csv\"");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter out = response.getWriter();
        out.write('﻿');
        out.println("Номер;Дата;Поставщик;ИНН;Склад;Статус;Ожидаемая дата;Сумма, руб.;Выполнено, %");
        Page<PurchaseOrder> page = orderService.search(status, supplierId, warehouseId, q,
                PageRequest.of(0, 10_000, Sort.by("orderDate").descending()));
        for (PurchaseOrder o : page) {
            out.println(String.join(";",
                    o.getNumber(),
                    o.getOrderDate().format(DATE),
                    csv(o.getSupplier().getName()),
                    o.getSupplier().getInn(),
                    csv(o.getWarehouse().getName()),
                    o.getStatus().getTitle(),
                    o.getExpectedDate() == null ? "" : o.getExpectedDate().format(DATE),
                    o.getTotalAmount().toPlainString().replace('.', ','),
                    String.valueOf(o.getProgress())));
        }
        out.flush();
    }

    private String showForm(OrderForm form, PurchaseOrder order, Model model) {
        model.addAttribute("form", form);
        model.addAttribute("order", order);
        model.addAttribute("suppliers", supplierService.findActive());
        model.addAttribute("warehouses", warehouseService.findAll());
        model.addAttribute("products", productService.findAll());
        return "orders/form";
    }

    private static String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
