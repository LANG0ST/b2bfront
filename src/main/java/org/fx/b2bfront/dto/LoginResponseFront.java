package org.fx.b2bfront.dto;

public class LoginResponseFront {
    public String token;
    public UserPayloadFront user;
    public boolean mustChangePassword;

    public static class UserPayloadFront {
        public Long id;
        public String email;
        public String role;
        public Long companyId;
    }
}
