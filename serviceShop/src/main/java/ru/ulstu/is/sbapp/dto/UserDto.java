package ru.ulstu.is.sbapp.dto;

import ru.ulstu.is.sbapp.model.Order;
import ru.ulstu.is.sbapp.model.User;
import ru.ulstu.is.sbapp.model.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDto {

    private final long id;

    private final String login;

    private String firstName;

    private String lastName;

    private final UserRole role;


    public UserDto(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();

    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public UserRole getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

