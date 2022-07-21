package ru.ulstu.is.sbapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.is.sbapp.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findRequestByName(String name);
}
