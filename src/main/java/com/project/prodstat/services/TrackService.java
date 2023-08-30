package com.project.prodstat.services;

import com.project.prodstat.entities.Track;
import com.project.prodstat.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TrackService {
    private final TrackRepository trackRepository;


    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    /**
     * Saves a track into the database.
     * @param track the track to save.
     * @return the track.
     */
    public Track save(Track track) {
        return trackRepository.save(track);
    }

    /**
     * Finds all the tracks.
     * @return a list of all the track.
     */
    public List<Track> findAll() {
        return trackRepository.findAll();
    }


    /**
     * Finds a track by its id.
     * @param id the id of the track.
     * @return an optional of the track.
     */
    public Optional<Track> findById(long id) {
        return trackRepository.findById(id);
    }


    /**
     * Finds a track by its id and removes it from the database.
     * @param id the id of the track.
     */
    public void deleteById(long id) {
        trackRepository.deleteAll(trackRepository.findAllById(List.of(id)));
    }
}
