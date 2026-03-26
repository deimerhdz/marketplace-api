package com.taurupro.marketplace.domain.dto;

public record ShippingAddressDto(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String zipCode
) {

}
