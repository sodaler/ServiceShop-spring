package ru.ulstu.is.sbapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.sbapp.model.OrderDetail;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
//    @Query("SELECT o From orders_details o Where o.request.name LIKE %:request%")
//    List<OrderDetail> findByNameContaining(@Param("request")String city);
}
