package com.project.prodstat.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Catalog {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "catalog_id",
            updatable = false
    )
    private Long id;


    @Column(
            name = "producer_id",
            updatable = false
    )
    private Long producerId;

    public Catalog() {
    }

    @ManyToMany
    @JoinTable(
            name = "catalog_track",
            joinColumns = @JoinColumn(name = "catalog_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private Set<Track> tracks = new HashSet<>();

    public Catalog(Long id, Long producerId, Set<Track> tracks) {
        this.id = id;
        this.producerId = producerId;
        this.tracks = tracks;
    }

    public Catalog(Long producerId, Set<Track> tracks) {
        this.producerId = producerId;
        this.tracks = tracks;
    }

    public Long getId() {
        return id;
    }

    public Long getProducerId() {
        return producerId;
    }

    public Set<Track> getTracks() {
        return tracks;
    }
}
