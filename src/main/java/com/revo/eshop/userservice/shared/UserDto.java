package com.revo.eshop.userservice.shared;

import com.revo.eshop.userservice.ui.model.orders.OrdersResponseModel;

import java.io.Serializable;
import java.util.List;

//Data transfer object
public class UserDto implements Serializable {
    private static final long serialVersionUID = -1717174613431342248L;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public List<OrdersResponseModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersResponseModel> orders) {
        this.orders = orders;
    }
}
