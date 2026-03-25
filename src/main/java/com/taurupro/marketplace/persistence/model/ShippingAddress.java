package com.taurupro.marketplace.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddress {
    private String firstName;
    private String lastName;
    private String email;
    private  String phone;
    private String address;
    private String city;
    private  String state;
    private String zipCode;
}
