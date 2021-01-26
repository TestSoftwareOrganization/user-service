package com.revo.eshop.userservice.ui.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {
    @NotNull(message = "Firstname cannot be null")
    @Size(min = 2, max = 20, message= "Firstname must be between 2 and 20 characters.")
    private String firstName;

    @NotNull(message = "Lastname cannot be null")
    @Size(min = 2, max = 20, message= "Lastname must be between 2 and 20 characters.")
    private String lastName;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 20, message= "Password must be between 6 and 20 characters.")
    private String password;

//    Does not work as expected
//    @Email
    @NotNull
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
