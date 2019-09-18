package com.flowrspot.service;

import com.flowrspot.domain.Sighting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sighting.
 */
public interface SightingService {

    /**
     * Save a sighting.
     *
     * @param sighting the entity to save
     * @return the persisted entity
     */
    Sighting save(Sighting sighting);

    /**
     * Get all the sightings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sighting> findAll(Pageable pageable);

    /**
     * Get the "id" sighting.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Sighting findOne(Long id);

    /**
     * Delete the "id" sighting.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
