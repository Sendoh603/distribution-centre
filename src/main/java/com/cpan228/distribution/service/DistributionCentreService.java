package com.cpan228.distribution.service;

import com.cpan228.distribution.dto.ItemRequestDTO;
import com.cpan228.distribution.model.DistributionCentre;
import com.cpan228.distribution.model.Item;
import com.cpan228.distribution.repository.DistributionCentreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionCentreService {
    private final DistributionCentreRepository distributionCentreRepository;

    public List<DistributionCentre> findAllCentres() {
        return distributionCentreRepository.findAll();
    }

    public List<DistributionCentre> findCentresWithItem(ItemRequestDTO request) {
        List<DistributionCentre> allCentres = distributionCentreRepository.findAll();
        log.info("Searching for item {} ({}) in {} centres",
                request.getName(), request.getBrand(), allCentres.size());

        return allCentres.stream()
                .filter(centre -> hasRequestedItem(centre, request))
                .sorted(Comparator.comparingDouble(centre ->
                        calculateDistance(
                                centre.getLatitude(),
                                centre.getLongitude(),
                                request.getRequestingLatitude(),
                                request.getRequestingLongitude()
                        )
                ))
                .peek(centre -> log.info("Found matching item at centre: {}", centre.getName()))
                .collect(Collectors.toList());
    }

    public Optional<Item> addItemToCentre(Long centreId, Item item) {
        return distributionCentreRepository.findById(centreId)
                .map(centre -> {
                    centre.getItems().add(item);
                    DistributionCentre savedCentre = distributionCentreRepository.save(centre);
                    return savedCentre.getItems().get(savedCentre.getItems().size() - 1);
                });
    }

    public void deleteItem(Long centreId, Long itemId) {
        distributionCentreRepository.findById(centreId).ifPresent(centre -> {
            centre.getItems().removeIf(item -> item.getId().equals(itemId));
            distributionCentreRepository.save(centre);
        });
    }

    private boolean hasRequestedItem(DistributionCentre centre, ItemRequestDTO request) {
        boolean hasItem = centre.getItems().stream()
                .anyMatch(item ->
                        item.getName().equals(request.getName()) &&
                                item.getBrand() == request.getBrand() &&
                                item.getQuantity() > 0
                );

        log.info("Centre {} {} the requested item",
                centre.getName(), hasItem ? "has" : "does not have");
        return hasItem;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public Optional<Item> processItemRequest(Long centreId, ItemRequestDTO request) {
        return distributionCentreRepository.findById(centreId)
                .flatMap(centre -> {
                    Optional<Item> item = centre.getItems().stream()
                            .filter(i -> i.getName().equals(request.getName()) &&
                                    i.getBrand() == request.getBrand() &&
                                    i.getQuantity() >= request.getQuantity())
                            .findFirst();

                    if (item.isPresent()) {
                        Item foundItem = item.get();
                        foundItem.setQuantity(foundItem.getQuantity() - request.getQuantity());
                        distributionCentreRepository.save(centre);

                        // Create a new item instance for the response
                        Item responseItem = new Item();
                        responseItem.setId(foundItem.getId());
                        responseItem.setName(foundItem.getName());
                        responseItem.setBrand(foundItem.getBrand());
                        responseItem.setYearOfCreation(foundItem.getYearOfCreation());
                        responseItem.setPrice(foundItem.getPrice());
                        responseItem.setQuantity(request.getQuantity());

                        return Optional.of(responseItem);
                    }
                    return Optional.empty();
                });
    }

}