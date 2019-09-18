package com.flowrspot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.flowrspot.domain.SightingLike;
import com.flowrspot.domain.User;
import com.flowrspot.security.SecurityUtils;
import com.flowrspot.service.SightingLikeService;
import com.flowrspot.service.UserService;
import com.flowrspot.web.rest.errors.BadRequestAlertException;
import com.flowrspot.web.rest.util.HeaderUtil;
import com.flowrspot.web.rest.util.PaginationUtil;
import com.flowrspot.service.dto.SightingLikeCriteria;
import com.flowrspot.service.SightingLikeQueryService;
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
 * REST controller for managing SightingLike.
 */
@RestController
@RequestMapping("/api")
public class SightingLikeResource {

    private final Logger log = LoggerFactory.getLogger(SightingLikeResource.class);

    private static final String ENTITY_NAME = "sightingLike";

    private final SightingLikeService sightingLikeService;

    private final SightingLikeQueryService sightingLikeQueryService;

    private final UserService userService;

    public SightingLikeResource(SightingLikeService sightingLikeService, SightingLikeQueryService sightingLikeQueryService, UserService userService) {
        this.sightingLikeService = sightingLikeService;
        this.sightingLikeQueryService = sightingLikeQueryService;
        this.userService = userService;
    }

    /**
     * POST  /sighting-likes : Create a new sightingLike.
     *
     * @param sightingLike the sightingLike to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sightingLike, or with status 400 (Bad Request) if the sightingLike has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sighting-likes")
    @Timed
    public ResponseEntity<SightingLike> createSightingLike(@RequestBody SightingLike sightingLike) throws URISyntaxException {
        log.debug("REST request to save SightingLike : {}", sightingLike);
        if (sightingLike.getId() != null) {
            throw new BadRequestAlertException("A new sightingLike cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SightingLike result = sightingLikeService.save(sightingLike);
        return ResponseEntity.created(new URI("/api/sighting-likes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sighting-likes : Updates an existing sightingLike.
     *
     * @param sightingLike the sightingLike to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sightingLike,
     * or with status 400 (Bad Request) if the sightingLike is not valid,
     * or with status 500 (Internal Server Error) if the sightingLike couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sighting-likes")
    @Timed
    public ResponseEntity<SightingLike> updateSightingLike(@RequestBody SightingLike sightingLike) throws URISyntaxException {
        log.debug("REST request to update SightingLike : {}", sightingLike);
        if (sightingLike.getId() == null) {
            return createSightingLike(sightingLike);
        }
        SightingLike result = sightingLikeService.save(sightingLike);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sightingLike.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sighting-likes : get all the sightingLikes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sightingLikes in body
     */
    @GetMapping("/sighting-likes")
    @Timed
    public ResponseEntity<List<SightingLike>> getAllSightingLikes(SightingLikeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SightingLikes by criteria: {}", criteria);
        Page<SightingLike> page = sightingLikeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sighting-likes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sighting-likes/:id : get the "id" sightingLike.
     *
     * @param id the id of the sightingLike to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sightingLike, or with status 404 (Not Found)
     */
    @GetMapping("/sighting-likes/{id}")
    @Timed
    public ResponseEntity<SightingLike> getSightingLike(@PathVariable Long id) {
        log.debug("REST request to get SightingLike : {}", id);
        SightingLike sightingLike = sightingLikeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sightingLike));
    }

    /**
     * DELETE  /sighting-likes/:id : delete the "id" sightingLike.
     *
     * @param id the id of the sightingLike to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sighting-likes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSightingLike(@PathVariable Long id) {
        log.debug("REST request to delete SightingLike : {}", id);
        sightingLikeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /my-likes : delete current user likes.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/my-likes")
    @Timed
    public ResponseEntity<Void> deleteMyLikes() {
        log.debug("REST request to delete current user Likes");
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userService::findOneByLogin);
        if(user.isPresent()){
            sightingLikeService.deleteByUser(user.get().getId());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, user.get().getEmail())).build();
    }
}
