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

import com.flowrspot.domain.Flower;
import com.flowrspot.domain.*; // for static metamodels
import com.flowrspot.repository.FlowerRepository;
import com.flowrspot.service.dto.FlowerCriteria;


/**
 * Service for executing complex queries for Flower entities in the database.
 * The main input is a {@link FlowerCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Flower} or a {@link Page} of {@link Flower} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlowerQueryService extends QueryService<Flower> {

    private final Logger log = LoggerFactory.getLogger(FlowerQueryService.class);


    private final FlowerRepository flowerRepository;

    public FlowerQueryService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    /**
     * Return a {@link List} of {@link Flower} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Flower> findByCriteria(FlowerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Flower> specification = createSpecification(criteria);
        return flowerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Flower} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Flower> findByCriteria(FlowerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Flower> specification = createSpecification(criteria);
        return flowerRepository.findAll(specification, page);
    }

    /**
     * Function to convert FlowerCriteria to a {@link Specifications}
     */
    private Specifications<Flower> createSpecification(FlowerCriteria criteria) {
        Specifications<Flower> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Flower_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Flower_.name));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Flower_.image));
            }
            if (criteria.getSightingsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSightingsId(), Flower_.sightings, Sighting_.id));
            }
        }
        return specification;
    }

}
