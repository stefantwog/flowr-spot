package com.flowrspot.service;

import com.flowrspot.domain.Flower;
import com.flowrspot.web.rest.vm.FlowerVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Flower.
 */
public interface FlowerService {

    /**
     * Save a flower.
     *
     * @param flower the entity to save
     * @return the persisted entity
     */
    Flower save(Flower flower);

    /**
     * Get all the flowers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Flower> findAll(Pageable pageable);

    /**
     * Get the "id" flower.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Flower findOne(Long id);

    /**
     * Delete the "id" flower.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Create flower out of VM model
     *
     * @return flower sighting
     */
    Flower createFlower(FlowerVM flower);
}
