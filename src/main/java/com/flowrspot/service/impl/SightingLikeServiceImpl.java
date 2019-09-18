package com.flowrspot.service.impl;

import com.flowrspot.service.SightingLikeService;
import com.flowrspot.domain.SightingLike;
import com.flowrspot.repository.SightingLikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SightingLike.
 */
@Service
@Transactional
public class SightingLikeServiceImpl implements SightingLikeService {

    private final Logger log = LoggerFactory.getLogger(SightingLikeServiceImpl.class);

    private final SightingLikeRepository sightingLikeRepository;

    public SightingLikeServiceImpl(SightingLikeRepository sightingLikeRepository) {
        this.sightingLikeRepository = sightingLikeRepository;
    }

    /**
     * Save a sightingLike.
     *
     * @param sightingLike the entity to save
     * @return the persisted entity
     */
    @Override
    public SightingLike save(SightingLike sightingLike) {
        log.debug("Request to save SightingLike : {}", sightingLike);
        return sightingLikeRepository.save(sightingLike);
    }

    /**
     * Get all the sightingLikes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SightingLike> findAll(Pageable pageable) {
        log.debug("Request to get all SightingLikes");
        return sightingLikeRepository.findAll(pageable);
    }

    /**
     * Get one sightingLike by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SightingLike findOne(Long id) {
        log.debug("Request to get SightingLike : {}", id);
        return sightingLikeRepository.findOne(id);
    }

    /**
     * Delete the sightingLike by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SightingLike : {}", id);
        sightingLikeRepository.delete(id);
    }

    @Override
    public void deleteBySighting(Long id) {
        sightingLikeRepository.deleteBySighting_Id(id);
    }

    @Override
    public void deleteByUser(Long id) {
        sightingLikeRepository.deleteByUser_Id(id);
    }
}
