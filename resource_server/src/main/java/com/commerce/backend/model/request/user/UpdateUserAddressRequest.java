package com.commerce.backend.model.request.user;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserAddressRequest {
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 100)
    private String city;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    private String state;

    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 5, max = 6)
    private String zip;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    private String country;

    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @Size(min = 3, max = 240)
    private String address;
}
