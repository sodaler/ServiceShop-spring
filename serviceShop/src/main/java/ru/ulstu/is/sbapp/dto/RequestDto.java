package ru.ulstu.is.sbapp.dto;

import ru.ulstu.is.sbapp.model.Order;

import java.util.List;

public class RequestDto {

    private Long id;

    private String name;

    private String description;

    private double price;

    private String type;

    private Long orderId;

    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orderName) {
        this.orders = orderName;
    }
}
