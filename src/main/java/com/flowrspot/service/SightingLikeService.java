package com.flowrspot.service;

import com.flowrspot.domain.Sighting;
import com.flowrspot.domain.SightingLike;
import com.flowrspot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SightingLike.
 */
public interface SightingLikeService {

    /**
     * Save a sightingLike.
     *
     * @param sightingLike the entity to save
     * @return the persisted entity
     */
    SightingLike save(SightingLike sightingLike);

    /**
     * Get all the sightingLikes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SightingLike> findAll(Pageable pageable);

    /**
     * Get the "id" sightingLike.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SightingLike findOne(Long id);

    /**
     * Delete the "id" sightingLike.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Delete the sighting likes by sighting.
     *
     * @param id the id of the sighting
     */
    void deleteBySighting(Long id);

    /**
     * Delete the sighting likes by user.
     *
     * @param id the id of the user
     */
    void deleteByUser(Long id);

    SightingLike likeSighting(Sighting sighting, User user);
}
