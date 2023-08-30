package com.project.prodstat.controllers;


import com.project.prodstat.DTOs.CatalogMetrics;
import com.project.prodstat.entities.Track;
import com.project.prodstat.exceptions.SpotifyException;
import com.project.prodstat.exceptions.YoutubeException;
import com.project.prodstat.services.CatalogService;
import com.project.prodstat.services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/add-track/{catalogId}")
    public Track searchTrack(@PathVariable Long catalogId, @RequestParam String trackName, @RequestParam String artistName) throws YoutubeException, SpotifyException {
        return catalogService.addTrack(catalogId,trackName, artistName);
    }


    @GetMapping("/catalog/{catalogId}/metrics")
    public ResponseEntity<CatalogMetrics> getCatalogMetrics(@PathVariable Long catalogId) {
        CatalogMetrics metrics = catalogService.calculateCatalogMetrics(catalogId);
        return ResponseEntity.ok(metrics);
    }


}
