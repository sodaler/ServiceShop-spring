package ru.ulstu.is.sbapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.is.sbapp.model.Order;
import ru.ulstu.is.sbapp.model.RequestType;

public interface OrderRepository extends JpaRepository<Order, Long> {
}