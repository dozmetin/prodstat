package com.project.prodstat.repositories;

import com.project.prodstat.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
