package com.cpan228.distribution.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Brand must be selected")
    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Min(value = 2022, message = "Year of creation must be more than 2021")
    private int yearOfCreation;

    @DecimalMin(value = "1000.01", message = "Price must be more than 1000")
    private double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    public enum Brand {
        BALENCIAGA, STONE_ISLAND, DIOR, GUCCI, PRADA, VERSACE
    }
}