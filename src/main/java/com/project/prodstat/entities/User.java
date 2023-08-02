package com.project.prodstat.entities;
import jakarta.persistence.*;

@Entity
public class User {
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
            name = "user_name",
            updatable = false
    )
    private String userName;


    @Column(
            name = "catalog_id",
            updatable = false
    )
    private Long catalog_id;


    public User(Long id, String userName, Long catalog_id) {
        this.id = id;
        this.userName = userName;
        this.catalog_id = catalog_id;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Long getCatalog_id() {
        return catalog_id;
    }
}
