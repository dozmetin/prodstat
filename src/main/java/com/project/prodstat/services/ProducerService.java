package com.project.prodstat.services;


import com.project.prodstat.entities.Catalog;
import com.project.prodstat.entities.Producer;
import com.project.prodstat.entities.Track;
import com.project.prodstat.repositories.CatalogRepository;
import com.project.prodstat.repositories.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProducerService {
    private final ProducerRepository producerRepository;
    private final CatalogRepository catalogRepository;


    @Autowired
    public ProducerService(ProducerRepository producerRepository, CatalogRepository catalogRepository) {
        this.producerRepository = producerRepository;
        this.catalogRepository = catalogRepository;
    }


    /**
     * Saves a producer into the database.
     * @param producer the producer to save.
     * @return the producer.
     */
    public Producer save(Producer producer) {
        return producerRepository.save(producer);
    }

    /**
     * Creates and saves a producer into the database.
     * @param producer the producer to create.
     * @return the producer.
     */
    public Producer createProducer(Producer producer) {
        Catalog catalog = new Catalog(producer.getId(), new HashSet<Track> ());
        catalogRepository.save(catalog);
        producer.setCatalog_id(catalog.getId());
        return producerRepository.save(producer);
    }

    /**
     * Finds all the producers.
     * @return a list of all the producers.
     */
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }


    /**
     * Finds a producer by its id.
     * @param id the id of the producer.
     * @return an optional of the producer.
     */
    public Optional<Producer> findById(long id) {
        return producerRepository.findById(id);
    }


    /**
     * Finds a producer by its id and removes it from the database.
     * @param id the id of the producer.
     */
    public void deleteById(long id) {
        producerRepository.deleteAll(producerRepository.findAllById(List.of(id)));
    }
}
