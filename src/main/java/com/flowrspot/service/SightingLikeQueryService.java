package com.flowrspot.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.flowrspot.domain.SightingLike;
import com.flowrspot.domain.*; // for static metamodels
import com.flowrspot.repository.SightingLikeRepository;
import com.flowrspot.service.dto.SightingLikeCriteria;


/**
 * Service for executing complex queries for SightingLike entities in the database.
 * The main input is a {@link SightingLikeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SightingLike} or a {@link Page} of {@link SightingLike} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SightingLikeQueryService extends QueryService<SightingLike> {

    private final Logger log = LoggerFactory.getLogger(SightingLikeQueryService.class);


    private final SightingLikeRepository sightingLikeRepository;

    public SightingLikeQueryService(SightingLikeRepository sightingLikeRepository) {
        this.sightingLikeRepository = sightingLikeRepository;
    }

    /**
     * Return a {@link List} of {@link SightingLike} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SightingLike> findByCriteria(SightingLikeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SightingLike> specification = createSpecification(criteria);
        return sightingLikeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SightingLike} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SightingLike> findByCriteria(SightingLikeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SightingLike> specification = createSpecification(criteria);
        return sightingLikeRepository.findAll(specification, page);
    }

    /**
     * Function to convert SightingLikeCriteria to a {@link Specifications}
     */
    private Specifications<SightingLike> createSpecification(SightingLikeCriteria criteria) {
        Specifications<SightingLike> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SightingLike_.id));
            }
            if (criteria.getSightingId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSightingId(), SightingLike_.sighting, Sighting_.id));
            }
        }
        return specification;
    }

}
