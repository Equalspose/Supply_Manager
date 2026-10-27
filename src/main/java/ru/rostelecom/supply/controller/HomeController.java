package ru.rostelecom.supply.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.rostelecom.supply.service.DashboardService;
import ru.rostelecom.supply.service.OrderService;
import ru.rostelecom.supply.service.StockService;

@Controller
public class HomeController {

    private final DashboardService dashboardService;
    private final StockService stockService;
    private final OrderService orderService;

    public HomeController(DashboardService dashboardService, StockService stockService, OrderService orderService) {
        this.dashboardService = dashboardService;
        this.stockService = stockService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.stats());
        model.addAttribute("recentOrders", dashboardService.recentOrders());
        model.addAttribute("lowStock", stockService.lowStock().stream().limit(6).toList());
        model.addAttribute("awaiting", orderService.awaitingDelivery().stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
