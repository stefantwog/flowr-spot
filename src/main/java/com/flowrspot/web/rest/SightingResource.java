package com.flowrspot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.flowrspot.domain.Sighting;
import com.flowrspot.service.SightingService;
import com.flowrspot.web.rest.errors.BadRequestAlertException;
import com.flowrspot.web.rest.util.HeaderUtil;
import com.flowrspot.web.rest.util.PaginationUtil;
import com.flowrspot.service.dto.SightingCriteria;
import com.flowrspot.service.SightingQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sighting.
 */
@RestController
@RequestMapping("/api")
public class SightingResource {

    private final Logger log = LoggerFactory.getLogger(SightingResource.class);

    private static final String ENTITY_NAME = "sighting";

    private final SightingService sightingService;

    private final SightingQueryService sightingQueryService;

    public SightingResource(SightingService sightingService, SightingQueryService sightingQueryService) {
        this.sightingService = sightingService;
        this.sightingQueryService = sightingQueryService;
    }

    /**
     * POST  /sightings : Create a new sighting.
     *
     * @param sighting the sighting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sighting, or with status 400 (Bad Request) if the sighting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sightings")
    @Timed
    public ResponseEntity<Sighting> createSighting(@RequestBody Sighting sighting) throws URISyntaxException {
        log.debug("REST request to save Sighting : {}", sighting);
        if (sighting.getId() != null) {
            throw new BadRequestAlertException("A new sighting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sighting result = sightingService.save(sighting);
        return ResponseEntity.created(new URI("/api/sightings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sightings : Updates an existing sighting.
     *
     * @param sighting the sighting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sighting,
     * or with status 400 (Bad Request) if the sighting is not valid,
     * or with status 500 (Internal Server Error) if the sighting couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sightings")
    @Timed
    public ResponseEntity<Sighting> updateSighting(@RequestBody Sighting sighting) throws URISyntaxException {
        log.debug("REST request to update Sighting : {}", sighting);
        if (sighting.getId() == null) {
            return createSighting(sighting);
        }
        Sighting result = sightingService.save(sighting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sighting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sightings : get all the sightings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sightings in body
     */
    @GetMapping("/sightings")
    @Timed
    public ResponseEntity<List<Sighting>> getAllSightings(SightingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sightings by criteria: {}", criteria);
        Page<Sighting> page = sightingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sightings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sightings/{flowerId} : get all the sightings by flower.
     *
     * @param flowerId id of flower
     * @return the ResponseEntity with status 200 (OK) and the list of sightings in body
     */
    @GetMapping("/sightings/{flowerId}")
    @Timed
    public ResponseEntity<List<Sighting>> getAllSightingsByFlower(@PathVariable Long flowerId) {
        log.debug("REST request to get Sightings by flower: {}", flowerId);
        List<Sighting> sightings = sightingService.findByFlower(flowerId);
        return new ResponseEntity<>(sightings, HttpStatus.OK);
    }

    /**
     * GET  /sightings/:id : get the "id" sighting.
     *
     * @param id the id of the sighting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sighting, or with status 404 (Not Found)
     */
    @GetMapping("/sightings/{id}")
    @Timed
    public ResponseEntity<Sighting> getSighting(@PathVariable Long id) {
        log.debug("REST request to get Sighting : {}", id);
        Sighting sighting = sightingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sighting));
    }

    /**
     * DELETE  /sightings/:id : delete the "id" sighting.
     *
     * @param id the id of the sighting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sightings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSighting(@PathVariable Long id) {
        log.debug("REST request to delete Sighting : {}", id);
        sightingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
