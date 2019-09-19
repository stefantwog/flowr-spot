package com.flowrspot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.flowrspot.domain.Flower;
import com.flowrspot.service.FlowerService;
import com.flowrspot.web.rest.errors.BadRequestAlertException;
import com.flowrspot.web.rest.util.HeaderUtil;
import com.flowrspot.web.rest.util.PaginationUtil;
import com.flowrspot.service.dto.FlowerCriteria;
import com.flowrspot.service.FlowerQueryService;
import com.flowrspot.web.rest.vm.FlowerVM;
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
 * REST controller for managing Flower.
 */
@RestController
@RequestMapping("/api")
public class FlowerResource {

    private final Logger log = LoggerFactory.getLogger(FlowerResource.class);

    private static final String ENTITY_NAME = "flower";

    private final FlowerService flowerService;

    private final FlowerQueryService flowerQueryService;

    public FlowerResource(FlowerService flowerService, FlowerQueryService flowerQueryService) {
        this.flowerService = flowerService;
        this.flowerQueryService = flowerQueryService;
    }

    /**
     * POST  /flowers : Create a new flower.
     *
     * @param flower the flower to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flower, or with status 400 (Bad Request) if the flower has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flowers")
    @Timed
    public ResponseEntity<Flower> createFlower(@RequestBody FlowerVM flower) throws URISyntaxException {
        log.debug("REST request to save Flower : {}", flower);
        if (flower.getId() != null) {
            throw new BadRequestAlertException("A new flower cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Flower result = flowerService.createFlower(flower);
        return ResponseEntity.created(new URI("/api/flowers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flowers : Updates an existing flower.
     *
     * @param flower the flower to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flower,
     * or with status 400 (Bad Request) if the flower is not valid,
     * or with status 500 (Internal Server Error) if the flower couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flowers")
    @Timed
    public ResponseEntity<Flower> updateFlower(@RequestBody Flower flower) throws URISyntaxException {
        log.debug("REST request to update Flower : {}", flower);
        Flower result = flowerService.save(flower);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, flower.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flowers : get all the flowers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of flowers in body
     */
    @GetMapping("/flowers")
    @Timed
    public ResponseEntity<List<Flower>> getAllFlowers(FlowerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Flowers by criteria: {}", criteria);
        Page<Flower> page = flowerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flowers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flowers/:id : get the "id" flower.
     *
     * @param id the id of the flower to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flower, or with status 404 (Not Found)
     */
    @GetMapping("/flowers/{id}")
    @Timed
    public ResponseEntity<Flower> getFlower(@PathVariable Long id) {
        log.debug("REST request to get Flower : {}", id);
        Flower flower = flowerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(flower));
    }

    /**
     * DELETE  /flowers/:id : delete the "id" flower.
     *
     * @param id the id of the flower to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flowers/{id}")
    @Timed
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id) {
        log.debug("REST request to delete Flower : {}", id);
        flowerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
