package com.iota.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotEmpty(message = "The email is required.")
    private String name;
    @NotNull(message = "The quantity is required.")
    private int quantity;
    @NotNull(message = "The cost is required.")
    private int cost;
    @NotEmpty(message = "The recipient is required.")
    private String recipient;
    @NotEmpty(message = "The custodian is required.")
    private String custodian;
    @NotEmpty(message = "The location is required.")
    private String location;
    @NotEmpty(message = "The delivery address is required.")
    private String deliveryAddress;
}
