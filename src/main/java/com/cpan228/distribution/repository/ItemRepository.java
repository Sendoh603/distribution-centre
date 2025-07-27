package com.cpan228.distribution.repository;

import com.cpan228.distribution.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByBrandAndName(Item.Brand brand, String name);
}