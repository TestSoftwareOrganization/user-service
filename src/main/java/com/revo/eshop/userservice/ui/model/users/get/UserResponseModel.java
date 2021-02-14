package com.revo.eshop.userservice.ui.model.users.get;

import com.revo.eshop.userservice.ui.model.orders.OrdersResponseModel;

import java.util.List;

public class UserResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrdersResponseModel> orders;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrdersResponseModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersResponseModel> orders) {
        this.orders = orders;
    }
}