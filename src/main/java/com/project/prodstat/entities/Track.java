package com.project.prodstat.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Track {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;


    @Column(
            name = "track_name",
            updatable = false
    )
    String trackName;


    @Column(
            name = "artist_name",
            updatable = false
    )
    String artistName;


    @ManyToMany(mappedBy = "tracks")
    private Set<Catalog> catalogs = new HashSet<>();

    @Column(
            name = "spotify_popularity"
    )
    int spotifyPopularity;


    @Column(
            name = "youtube_views"
    )
    long youtubeViewCount;

    public Track(String trackName, String artistName, List<Long> catalogIds, int spotifyPopularity, long youtubeViewCount) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.spotifyPopularity = spotifyPopularity;
        this.youtubeViewCount = youtubeViewCount;
    }

    public Track() {
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getSpotifyPopularity() {
        return spotifyPopularity;
    }

    public void setSpotifyPopularity(int spotifyPopularity) {
        this.spotifyPopularity = spotifyPopularity;
    }

    public long getYoutubeViewCount() {
        return youtubeViewCount;
    }

    public void setYoutubeViewCount(long youtubeViewCount) {
        this.youtubeViewCount = youtubeViewCount;
    }
}
