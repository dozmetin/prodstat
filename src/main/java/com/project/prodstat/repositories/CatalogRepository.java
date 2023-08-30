package com.project.prodstat.repositories;

import com.project.prodstat.entities.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
}
