package com.cpan228.distribution.dto;

import com.cpan228.distribution.model.Item;
import lombok.Data;

@Data
public class ItemRequestDTO {
    private Item.Brand brand;
    private String name;
    private Double requestingLatitude;
    private Double requestingLongitude;
    private int quantity;
}