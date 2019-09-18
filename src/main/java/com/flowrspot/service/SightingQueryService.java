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

import com.flowrspot.domain.Sighting;
import com.flowrspot.domain.*; // for static metamodels
import com.flowrspot.repository.SightingRepository;
import com.flowrspot.service.dto.SightingCriteria;


/**
 * Service for executing complex queries for Sighting entities in the database.
 * The main input is a {@link SightingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sighting} or a {@link Page} of {@link Sighting} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SightingQueryService extends QueryService<Sighting> {

    private final Logger log = LoggerFactory.getLogger(SightingQueryService.class);


    private final SightingRepository sightingRepository;

    public SightingQueryService(SightingRepository sightingRepository) {
        this.sightingRepository = sightingRepository;
    }

    /**
     * Return a {@link List} of {@link Sighting} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sighting> findByCriteria(SightingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Sighting> specification = createSpecification(criteria);
        return sightingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sighting} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sighting> findByCriteria(SightingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Sighting> specification = createSpecification(criteria);
        return sightingRepository.findAll(specification, page);
    }

    /**
     * Function to convert SightingCriteria to a {@link Specifications}
     */
    private Specifications<Sighting> createSpecification(SightingCriteria criteria) {
        Specifications<Sighting> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sighting_.id));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Sighting_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Sighting_.latitude));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Sighting_.image));
            }
            if (criteria.getFlowerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFlowerId(), Sighting_.flower, Flower_.id));
            }
        }
        return specification;
    }

}
