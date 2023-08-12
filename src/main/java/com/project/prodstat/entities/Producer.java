package com.project.prodstat.entities;
import jakarta.persistence.*;

@Entity
public class Producer {
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
            name = "producer_name",
            updatable = false
    )
    private String producerName;


    @Column(
            name = "catalog_id"
    )
    private Long catalog_id;

    public Producer() {
    }

    public Producer(Long id, String producerName, Long catalog_id) {
        this.id = id;
        this.producerName = producerName;
        this.catalog_id = catalog_id;
    }

    public Long getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setCatalog_id(Long catalog_id) {
        this.catalog_id = catalog_id;
    }

    public Long getCatalog_id() {
        return catalog_id;
    }
}
