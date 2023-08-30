package com.project.prodstat.DTOs;

public class CatalogMetrics {
    private double averageSpotifyPopularity;
    private long totalYoutubeViews;

    // Constructors, getters, setters, etc.

    public double getAverageSpotifyPopularity() {
        return averageSpotifyPopularity;
    }

    public void setAverageSpotifyPopularity(double averageSpotifyPopularity) {
        this.averageSpotifyPopularity = averageSpotifyPopularity;
    }

    public long getTotalYoutubeViews() {
        return totalYoutubeViews;
    }

    public void setTotalYoutubeViews(long totalYoutubeViews) {
        this.totalYoutubeViews = totalYoutubeViews;
    }
}

