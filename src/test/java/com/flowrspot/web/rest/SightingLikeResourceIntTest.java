package com.flowrspot.web.rest;

import com.flowrspot.FlowrSpotApp;

import com.flowrspot.domain.SightingLike;
import com.flowrspot.domain.Sighting;
import com.flowrspot.repository.SightingLikeRepository;
import com.flowrspot.service.SightingLikeService;
import com.flowrspot.web.rest.errors.ExceptionTranslator;
import com.flowrspot.service.dto.SightingLikeCriteria;
import com.flowrspot.service.SightingLikeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.flowrspot.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SightingLikeResource REST controller.
 *
 * @see SightingLikeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlowrSpotApp.class)
public class SightingLikeResourceIntTest {

    @Autowired
    private SightingLikeRepository sightingLikeRepository;

    @Autowired
    private SightingLikeService sightingLikeService;

    @Autowired
    private SightingLikeQueryService sightingLikeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSightingLikeMockMvc;

    private SightingLike sightingLike;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SightingLikeResource sightingLikeResource = new SightingLikeResource(sightingLikeService, sightingLikeQueryService);
        this.restSightingLikeMockMvc = MockMvcBuilders.standaloneSetup(sightingLikeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SightingLike createEntity(EntityManager em) {
        SightingLike sightingLike = new SightingLike();
        return sightingLike;
    }

    @Before
    public void initTest() {
        sightingLike = createEntity(em);
    }

    @Test
    @Transactional
    public void createSightingLike() throws Exception {
        int databaseSizeBeforeCreate = sightingLikeRepository.findAll().size();

        // Create the SightingLike
        restSightingLikeMockMvc.perform(post("/api/sighting-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sightingLike)))
            .andExpect(status().isCreated());

        // Validate the SightingLike in the database
        List<SightingLike> sightingLikeList = sightingLikeRepository.findAll();
        assertThat(sightingLikeList).hasSize(databaseSizeBeforeCreate + 1);
        SightingLike testSightingLike = sightingLikeList.get(sightingLikeList.size() - 1);
    }

    @Test
    @Transactional
    public void createSightingLikeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sightingLikeRepository.findAll().size();

        // Create the SightingLike with an existing ID
        sightingLike.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSightingLikeMockMvc.perform(post("/api/sighting-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sightingLike)))
            .andExpect(status().isBadRequest());

        // Validate the SightingLike in the database
        List<SightingLike> sightingLikeList = sightingLikeRepository.findAll();
        assertThat(sightingLikeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSightingLikes() throws Exception {
        // Initialize the database
        sightingLikeRepository.saveAndFlush(sightingLike);

        // Get all the sightingLikeList
        restSightingLikeMockMvc.perform(get("/api/sighting-likes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sightingLike.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSightingLike() throws Exception {
        // Initialize the database
        sightingLikeRepository.saveAndFlush(sightingLike);

        // Get the sightingLike
        restSightingLikeMockMvc.perform(get("/api/sighting-likes/{id}", sightingLike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sightingLike.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllSightingLikesBySightingIsEqualToSomething() throws Exception {
        // Initialize the database
        Sighting sighting = SightingResourceIntTest.createEntity(em);
        em.persist(sighting);
        em.flush();
        sightingLike.setSighting(sighting);
        sightingLikeRepository.saveAndFlush(sightingLike);
        Long sightingId = sighting.getId();

        // Get all the sightingLikeList where sighting equals to sightingId
        defaultSightingLikeShouldBeFound("sightingId.equals=" + sightingId);

        // Get all the sightingLikeList where sighting equals to sightingId + 1
        defaultSightingLikeShouldNotBeFound("sightingId.equals=" + (sightingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSightingLikeShouldBeFound(String filter) throws Exception {
        restSightingLikeMockMvc.perform(get("/api/sighting-likes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sightingLike.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSightingLikeShouldNotBeFound(String filter) throws Exception {
        restSightingLikeMockMvc.perform(get("/api/sighting-likes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSightingLike() throws Exception {
        // Get the sightingLike
        restSightingLikeMockMvc.perform(get("/api/sighting-likes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSightingLike() throws Exception {
        // Initialize the database
        sightingLikeService.save(sightingLike);

        int databaseSizeBeforeUpdate = sightingLikeRepository.findAll().size();

        // Update the sightingLike
        SightingLike updatedSightingLike = sightingLikeRepository.findOne(sightingLike.getId());
        // Disconnect from session so that the updates on updatedSightingLike are not directly saved in db
        em.detach(updatedSightingLike);

        restSightingLikeMockMvc.perform(put("/api/sighting-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSightingLike)))
            .andExpect(status().isOk());

        // Validate the SightingLike in the database
        List<SightingLike> sightingLikeList = sightingLikeRepository.findAll();
        assertThat(sightingLikeList).hasSize(databaseSizeBeforeUpdate);
        SightingLike testSightingLike = sightingLikeList.get(sightingLikeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSightingLike() throws Exception {
        int databaseSizeBeforeUpdate = sightingLikeRepository.findAll().size();

        // Create the SightingLike

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSightingLikeMockMvc.perform(put("/api/sighting-likes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sightingLike)))
            .andExpect(status().isCreated());

        // Validate the SightingLike in the database
        List<SightingLike> sightingLikeList = sightingLikeRepository.findAll();
        assertThat(sightingLikeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSightingLike() throws Exception {
        // Initialize the database
        sightingLikeService.save(sightingLike);

        int databaseSizeBeforeDelete = sightingLikeRepository.findAll().size();

        // Get the sightingLike
        restSightingLikeMockMvc.perform(delete("/api/sighting-likes/{id}", sightingLike.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SightingLike> sightingLikeList = sightingLikeRepository.findAll();
        assertThat(sightingLikeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SightingLike.class);
        SightingLike sightingLike1 = new SightingLike();
        sightingLike1.setId(1L);
        SightingLike sightingLike2 = new SightingLike();
        sightingLike2.setId(sightingLike1.getId());
        assertThat(sightingLike1).isEqualTo(sightingLike2);
        sightingLike2.setId(2L);
        assertThat(sightingLike1).isNotEqualTo(sightingLike2);
        sightingLike1.setId(null);
        assertThat(sightingLike1).isNotEqualTo(sightingLike2);
    }
}
