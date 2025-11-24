package com.pasteleria.pasteleria_backend.controller;

import com.pasteleria.pasteleria_backend.model.*;
import com.pasteleria.pasteleria_backend.repository.OrderRepository;
import com.pasteleria.pasteleria_backend.repository.ProductRepository;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import com.pasteleria.pasteleria_backend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<Order> getMine(Authentication authentication) {
        var user = resolveUser(authentication);
        return orderRepository.findByUserId(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Order create(@Valid @RequestBody OrderCreateRequest request,
                        Authentication authentication) {

        var user = resolveUser(authentication);
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe contener al menos un producto");
        }

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            var product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Producto no encontrado: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());

            items.add(item);
            total += product.getPrice() * itemRequest.getQuantity();
        }

        // Aplicar descuento si existe
        double discountAmount = (request.getDiscountAmount() != null) ? request.getDiscountAmount() : 0.0;
        double finalTotal = total - discountAmount;
        if (finalTotal < 0) {
            finalTotal = 0.0;
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setUserEmail(user.getEmail());
        order.setItems(items);
        order.setTotal(finalTotal);
        order.setStatus("PENDIENTE");
        order.setDiscountAmount(discountAmount);
        order.setDiscountPercentage(request.getDiscountPercentage());
        order.setDiscountDescription(request.getDiscountDescription());

        return orderRepository.save(order);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(@PathVariable Long orderId,
                              @Valid @RequestBody OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

    private User resolveUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no registrado");
        }
        return user;
    }
}

