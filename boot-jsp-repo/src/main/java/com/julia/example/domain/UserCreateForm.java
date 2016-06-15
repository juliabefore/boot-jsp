package com.julia.example.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserCreateForm {

    @NotEmpty
    @Size(max = 64)
    private String email = "";

    @NotEmpty
    @Size(max = 64)
    private String password = "";

    @NotEmpty
    @Size(max = 64)
    private String passwordRepeated = "";

    @NotNull
    private Role role = Role.USER;

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

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserCreateForm{" +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordRepeated='" + passwordRepeated + '\'' +
                ", role=" + role +
                '}';
    }

}
