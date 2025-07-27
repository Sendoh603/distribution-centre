package com.cpan228.distribution.config;

import com.cpan228.distribution.model.DistributionCentre;
import com.cpan228.distribution.model.Item;
import com.cpan228.distribution.repository.DistributionCentreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Profile({"dev", "qa"}) // Add qa profile
    public CommandLineRunner initData(DistributionCentreRepository repository) {
        return args -> {
            // Check if data already exists to avoid duplicate initialization
            if (repository.count() == 0) {
                // GTA area distribution centres
                DistributionCentre scarborough = new DistributionCentre(
                        null,
                        "Scarborough DC",
                        43.7731,
                        -79.2579,
                        createScarboroughItems()
                );

                DistributionCentre mississauga = new DistributionCentre(
                        null,
                        "Mississauga DC",
                        43.5890,
                        -79.6441,
                        createMississaugaItems()
                );

                DistributionCentre vaughan = new DistributionCentre(
                        null,
                        "Vaughan DC",
                        43.8361,
                        -79.4983,
                        createVaughanItems()
                );

                DistributionCentre markham = new DistributionCentre(
                        null,
                        "Markham DC",
                        43.8561,
                        -79.3370,
                        createMarkhamItems()
                );

                repository.saveAll(Arrays.asList(
                        scarborough, mississauga, vaughan, markham
                ));
            }
        };
    }

    private List<Item> createScarboroughItems() {
        return Arrays.asList(
            new Item(null, "Sport T-Shirt", Item.Brand.BALENCIAGA, 2023, 1200.0, 25),
            new Item(null, "Logo T-Shirt", Item.Brand.STONE_ISLAND, 2023, 1500.0, 40),
            new Item(null, "Running Shoes", Item.Brand.STONE_ISLAND, 2023, 2000.0, 15),
            new Item(null, "Training Pants", Item.Brand.VERSACE, 2023, 1800.0, 30)
        );
    }

    private List<Item> createMississaugaItems() {
        return Arrays.asList(
            new Item(null, "Sport T-Shirt", Item.Brand.BALENCIAGA, 2023, 1200.0, 15),
            new Item(null, "Logo T-Shirt", Item.Brand.GUCCI, 2023, 1500.0, 20),
            new Item(null, "Running Shoes", Item.Brand.VERSACE, 2023, 2000.0, 35),
            new Item(null, "Training Pants", Item.Brand.VERSACE, 2023, 1800.0, 25)
        );
    }

    private List<Item> createVaughanItems() {
        return Arrays.asList(
            new Item(null, "Sport T-Shirt", Item.Brand.BALENCIAGA, 2023, 1200.0, 30),
            new Item(null, "Logo T-Shirt", Item.Brand.GUCCI, 2023, 1500.0, 35),
            new Item(null, "Running Shoes", Item.Brand.STONE_ISLAND, 2023, 2000.0, 20),
            new Item(null, "Training Pants", Item.Brand.PRADA, 2023, 1800.0, 15)
        );
    }

    private List<Item> createMarkhamItems() {
        return Arrays.asList(
            new Item(null, "Sport T-Shirt", Item.Brand.GUCCI, 2023, 1200.0, 20),
            new Item(null, "Logo T-Shirt", Item.Brand.GUCCI, 2023, 1500.0, 30),
            new Item(null, "Running Shoes", Item.Brand.STONE_ISLAND, 2023, 2000.0, 25),
            new Item(null, "Training Pants", Item.Brand.VERSACE, 2023, 1800.0, 20)
        );
    }
}