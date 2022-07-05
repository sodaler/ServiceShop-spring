package ru.ulstu.is.sbapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.is.sbapp.model.Request;
import ru.ulstu.is.sbapp.model.RequestType;

public interface RequestTypeRepository extends JpaRepository<RequestType, Long> {
    RequestType findRequestTypeByName(String name);
//    RequestType findRequestTypeByPrice(String price);
}
