package org.fx.b2bfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestFront {

    private String name;
    private String ice;
    private String email;
    private String password;
    private String address;
    private String city;
    private String phone;

}
