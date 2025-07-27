package com.cpan228.distribution.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    public String getAddress() {
        return String.format("%s (%.4f, %.4f)", name, latitude, longitude);
    }
}