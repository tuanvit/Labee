package com.labee.service;

import com.labee.dto.request.PlaceOrderRequest;
import com.labee.dto.response.OrderItemResponse;
import com.labee.dto.response.OrderResponse;
import com.labee.exception.BadRequestException;
import com.labee.exception.ResourceNotFoundException;
import com.labee.model.entity.*;
import com.labee.model.enums.OrderStatus;
import com.labee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse placeOrder(String userId, PlaceOrderRequest request) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify address exists and belongs to user
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to address");
        }

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Create order (orderId will be auto-generated)
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .isPaid(false)
                .totalAmount(BigDecimal.ZERO)
                .shippingFee(BigDecimal.ZERO)
                .finalAmount(BigDecimal.ZERO)
                .shippingAddress(formatAddress(address))
                .shippingName(address.getRecipientName())
                .shippingPhone(address.getPhoneNumber())
                .build();

        // Save order first to get generated ID
        Order savedOrder = orderRepository.save(order);

        // Create order items and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();

                    // Check stock availability
                    if (product.getStockQuantity() < cartItem.getQuantity()) {
                        throw new BadRequestException("Insufficient stock for product: " + product.getName() +
                                ". Available: " + product.getStockQuantity());
                    }

                    // Deduct stock
                    product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                    productRepository.save(product);

                    // Create order item with price snapshot (orderItemId will be auto-generated)
                    OrderItem orderItem = OrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .productName(product.getName())
                            .productImageUrl(product.getImageUrl())
                            .priceAtOrder(product.getPrice())
                            .quantity(cartItem.getQuantity())
                            .build();

                    return orderItem;
                })
                .collect(Collectors.toList());

        // Calculate total amount
        totalAmount = orderItems.stream()
                .map(item -> item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setFinalAmount(totalAmount.add(savedOrder.getShippingFee()));

        // Update order with calculated amounts
        Order finalOrder = orderRepository.save(savedOrder);
        orderItemRepository.saveAll(orderItems);

        // Clear cart
        cartItemRepository.deleteByUser(user);

        return mapToOrderResponse(finalOrder, orderItems);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(String userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findByUser(user, pageable);

        return orders.map(order -> {
            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
            return mapToOrderResponse(order, orderItems);
        });
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(String userId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify ownership
        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to order");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        return mapToOrderResponse(order, orderItems);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "ORD" + timestamp + random;
    }

    private String formatAddress(Address address) {
        return String.format("%s, %s, %s, %s",
                address.getDetailAddress(),
                address.getWard(),
                address.getDistrict(),
                address.getProvince());
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderItem> orderItems) {
        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> {
                    BigDecimal itemTotal = item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return OrderItemResponse.builder()
                            .orderItemId(item.getOrderItemId())
                            .productId(item.getProduct().getProductId())
                            .productName(item.getProductName())
                            .productImage(item.getProductImageUrl())
                            .priceSnapshot(item.getPriceAtOrder())
                            .quantity(item.getQuantity())
                            .itemTotal(itemTotal)
                            .build();
                })
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().name())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getIsPaid() ? "PAID" : "PENDING")
                .totalAmount(order.getFinalAmount())
                .shippingAddress(order.getShippingAddress())
                .recipientName(order.getShippingName())
                .recipientPhone(order.getShippingPhone())
                .orderedAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}
