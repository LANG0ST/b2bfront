package org.fx.b2bfront.dto;

public class LoginRequestFront {
    private String email;
    private String password;

    public LoginRequestFront(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
