package com.cpan228.distribution.controller;

import com.cpan228.distribution.model.DistributionCentre;
import com.cpan228.distribution.model.Item;
import com.cpan228.distribution.dto.ItemRequestDTO;
import com.cpan228.distribution.service.DistributionCentreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/distribution-centres")
@RequiredArgsConstructor
public class DistributionCentreController {
    private final DistributionCentreService distributionCentreService;

    @GetMapping
    public List<DistributionCentre> getAllCentres() {
        return distributionCentreService.findAllCentres();
    }

    @PostMapping("/request-item")
    public ResponseEntity<List<DistributionCentre>> findCentresWithItem(@RequestBody ItemRequestDTO request) {
        log.info("Received request for item: {} ({})", request.getName(), request.getBrand());
        List<DistributionCentre> centres = distributionCentreService.findCentresWithItem(request);
        log.info("Found {} centres with the requested item", centres.size());
        return ResponseEntity.ok(centres);
    }

    @PostMapping("/{centreId}/items")
    public ResponseEntity<Item> addItem(@PathVariable Long centreId, @RequestBody Item item) {
        return distributionCentreService.addItemToCentre(centreId, item)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{centreId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long centreId, @PathVariable Long itemId) {
        distributionCentreService.deleteItem(centreId, itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{centreId}/request-item")
    public ResponseEntity<Item> requestItemFromCentre(
            @PathVariable Long centreId,
            @RequestBody ItemRequestDTO request) {
        log.info("Received request for item from centre {}: {} ({})",
                centreId, request.getName(), request.getBrand());

        return distributionCentreService.processItemRequest(centreId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}