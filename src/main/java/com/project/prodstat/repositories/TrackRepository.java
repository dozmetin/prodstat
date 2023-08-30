package com.project.prodstat.repositories;

import com.project.prodstat.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
