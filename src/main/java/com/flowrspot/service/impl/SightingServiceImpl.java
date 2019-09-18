package com.flowrspot.service.impl;

import com.flowrspot.service.SightingService;
import com.flowrspot.domain.Sighting;
import com.flowrspot.repository.SightingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Sighting.
 */
@Service
@Transactional
public class SightingServiceImpl implements SightingService {

    private final Logger log = LoggerFactory.getLogger(SightingServiceImpl.class);

    private final SightingRepository sightingRepository;

    public SightingServiceImpl(SightingRepository sightingRepository) {
        this.sightingRepository = sightingRepository;
    }

    /**
     * Save a sighting.
     *
     * @param sighting the entity to save
     * @return the persisted entity
     */
    @Override
    public Sighting save(Sighting sighting) {
        log.debug("Request to save Sighting : {}", sighting);
        return sightingRepository.save(sighting);
    }

    /**
     * Get all the sightings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sighting> findAll(Pageable pageable) {
        log.debug("Request to get all Sightings");
        return sightingRepository.findAll(pageable);
    }

    /**
     * Get one sighting by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Sighting findOne(Long id) {
        log.debug("Request to get Sighting : {}", id);
        return sightingRepository.findOne(id);
    }

    /**
     * Delete the sighting by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sighting : {}", id);
        sightingRepository.delete(id);
    }

    @Override
    public List<Sighting> findByFlower(Long flowerId) {
        return sightingRepository.findByFlower_Id(flowerId);
    }
}
