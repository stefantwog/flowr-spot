package com.flowrspot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.flowrspot.domain.Sighting;
import com.flowrspot.domain.User;
import com.flowrspot.security.SecurityUtils;
import com.flowrspot.service.SightingService;
import com.flowrspot.service.UserService;
import com.flowrspot.web.rest.errors.BadRequestAlertException;
import com.flowrspot.web.rest.util.HeaderUtil;
import com.flowrspot.web.rest.util.PaginationUtil;
import com.flowrspot.service.dto.SightingCriteria;
import com.flowrspot.service.SightingQueryService;
import com.flowrspot.web.rest.vm.SightingVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
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

    private final UserService userService;

    public SightingResource(SightingService sightingService, SightingQueryService sightingQueryService, UserService userService) {
        this.sightingService = sightingService;
        this.sightingQueryService = sightingQueryService;
        this.userService = userService;
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
    public ResponseEntity<Sighting> createSighting(@RequestBody SightingVM sighting) throws URISyntaxException {
        log.debug("REST request to save Sighting : {}", sighting);
        if (sighting.getId() != null) {
            throw new BadRequestAlertException("A new sighting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userService::findOneByLogin);
        Sighting result = sightingService.createSighting(sighting, user.get());
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
     * GET  /flower-sighting/{flowerId} : get all the sightings by flower.
     *
     * @param flowerId id of flower
     * @return the ResponseEntity with status 200 (OK) and the list of sightings in body
     */
    @GetMapping("/flower-sightings/{flowerId}")
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
     * DELETE  /my-sightings : delete current user sightings.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/my-sightings")
    @Timed
    public ResponseEntity<Void> deleteMySighting() {
        log.debug("REST request to delete current user Sighting");
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userService::findOneByLogin);
        if(user.isPresent()){
            sightingService.deleteByUser(user.get().getId());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, user.get().getEmail())).build();
    }
}
