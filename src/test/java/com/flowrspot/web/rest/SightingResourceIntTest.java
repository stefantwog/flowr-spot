package com.flowrspot.web.rest;

import com.flowrspot.FlowrSpotApp;

import com.flowrspot.domain.Sighting;
import com.flowrspot.domain.SightingLike;
import com.flowrspot.domain.Flower;
import com.flowrspot.repository.SightingRepository;
import com.flowrspot.service.SightingService;
import com.flowrspot.web.rest.errors.ExceptionTranslator;
import com.flowrspot.service.dto.SightingCriteria;
import com.flowrspot.service.SightingQueryService;

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
 * Test class for the SightingResource REST controller.
 *
 * @see SightingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlowrSpotApp.class)
public class SightingResourceIntTest {

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private SightingService sightingService;

    @Autowired
    private SightingQueryService sightingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSightingMockMvc;

    private Sighting sighting;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SightingResource sightingResource = new SightingResource(sightingService, sightingQueryService);
        this.restSightingMockMvc = MockMvcBuilders.standaloneSetup(sightingResource)
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
    public static Sighting createEntity(EntityManager em) {
        Sighting sighting = new Sighting()
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .image(DEFAULT_IMAGE);
        return sighting;
    }

    @Before
    public void initTest() {
        sighting = createEntity(em);
    }

    @Test
    @Transactional
    public void createSighting() throws Exception {
        int databaseSizeBeforeCreate = sightingRepository.findAll().size();

        // Create the Sighting
        restSightingMockMvc.perform(post("/api/sightings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sighting)))
            .andExpect(status().isCreated());

        // Validate the Sighting in the database
        List<Sighting> sightingList = sightingRepository.findAll();
        assertThat(sightingList).hasSize(databaseSizeBeforeCreate + 1);
        Sighting testSighting = sightingList.get(sightingList.size() - 1);
        assertThat(testSighting.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testSighting.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testSighting.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void createSightingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sightingRepository.findAll().size();

        // Create the Sighting with an existing ID
        sighting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSightingMockMvc.perform(post("/api/sightings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sighting)))
            .andExpect(status().isBadRequest());

        // Validate the Sighting in the database
        List<Sighting> sightingList = sightingRepository.findAll();
        assertThat(sightingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSightings() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList
        restSightingMockMvc.perform(get("/api/sightings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sighting.getId().intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())));
    }

    @Test
    @Transactional
    public void getSighting() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get the sighting
        restSightingMockMvc.perform(get("/api/sightings/{id}", sighting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sighting.getId().intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getAllSightingsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where longitude equals to DEFAULT_LONGITUDE
        defaultSightingShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the sightingList where longitude equals to UPDATED_LONGITUDE
        defaultSightingShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllSightingsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultSightingShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the sightingList where longitude equals to UPDATED_LONGITUDE
        defaultSightingShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllSightingsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where longitude is not null
        defaultSightingShouldBeFound("longitude.specified=true");

        // Get all the sightingList where longitude is null
        defaultSightingShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllSightingsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where latitude equals to DEFAULT_LATITUDE
        defaultSightingShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the sightingList where latitude equals to UPDATED_LATITUDE
        defaultSightingShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllSightingsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultSightingShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the sightingList where latitude equals to UPDATED_LATITUDE
        defaultSightingShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllSightingsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where latitude is not null
        defaultSightingShouldBeFound("latitude.specified=true");

        // Get all the sightingList where latitude is null
        defaultSightingShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllSightingsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where image equals to DEFAULT_IMAGE
        defaultSightingShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the sightingList where image equals to UPDATED_IMAGE
        defaultSightingShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllSightingsByImageIsInShouldWork() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultSightingShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the sightingList where image equals to UPDATED_IMAGE
        defaultSightingShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllSightingsByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        sightingRepository.saveAndFlush(sighting);

        // Get all the sightingList where image is not null
        defaultSightingShouldBeFound("image.specified=true");

        // Get all the sightingList where image is null
        defaultSightingShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    public void getAllSightingsByLikesIsEqualToSomething() throws Exception {
        // Initialize the database
        SightingLike likes = SightingLikeResourceIntTest.createEntity(em);
        em.persist(likes);
        em.flush();
        sightingRepository.saveAndFlush(sighting);
        Long likesId = likes.getId();

        // Get all the sightingList where likes equals to likesId
        defaultSightingShouldBeFound("likesId.equals=" + likesId);

        // Get all the sightingList where likes equals to likesId + 1
        defaultSightingShouldNotBeFound("likesId.equals=" + (likesId + 1));
    }


    @Test
    @Transactional
    public void getAllSightingsByFlowerIsEqualToSomething() throws Exception {
        // Initialize the database
        Flower flower = FlowerResourceIntTest.createEntity(em);
        em.persist(flower);
        em.flush();
        sighting.setFlower(flower);
        sightingRepository.saveAndFlush(sighting);
        Long flowerId = flower.getId();

        // Get all the sightingList where flower equals to flowerId
        defaultSightingShouldBeFound("flowerId.equals=" + flowerId);

        // Get all the sightingList where flower equals to flowerId + 1
        defaultSightingShouldNotBeFound("flowerId.equals=" + (flowerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSightingShouldBeFound(String filter) throws Exception {
        restSightingMockMvc.perform(get("/api/sightings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sighting.getId().intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSightingShouldNotBeFound(String filter) throws Exception {
        restSightingMockMvc.perform(get("/api/sightings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSighting() throws Exception {
        // Get the sighting
        restSightingMockMvc.perform(get("/api/sightings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSighting() throws Exception {
        // Initialize the database
        sightingService.save(sighting);

        int databaseSizeBeforeUpdate = sightingRepository.findAll().size();

        // Update the sighting
        Sighting updatedSighting = sightingRepository.findOne(sighting.getId());
        // Disconnect from session so that the updates on updatedSighting are not directly saved in db
        em.detach(updatedSighting);
        updatedSighting
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .image(UPDATED_IMAGE);

        restSightingMockMvc.perform(put("/api/sightings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSighting)))
            .andExpect(status().isOk());

        // Validate the Sighting in the database
        List<Sighting> sightingList = sightingRepository.findAll();
        assertThat(sightingList).hasSize(databaseSizeBeforeUpdate);
        Sighting testSighting = sightingList.get(sightingList.size() - 1);
        assertThat(testSighting.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSighting.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testSighting.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingSighting() throws Exception {
        int databaseSizeBeforeUpdate = sightingRepository.findAll().size();

        // Create the Sighting

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSightingMockMvc.perform(put("/api/sightings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sighting)))
            .andExpect(status().isCreated());

        // Validate the Sighting in the database
        List<Sighting> sightingList = sightingRepository.findAll();
        assertThat(sightingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSighting() throws Exception {
        // Initialize the database
        sightingService.save(sighting);

        int databaseSizeBeforeDelete = sightingRepository.findAll().size();

        // Get the sighting
        restSightingMockMvc.perform(delete("/api/sightings/{id}", sighting.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sighting> sightingList = sightingRepository.findAll();
        assertThat(sightingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sighting.class);
        Sighting sighting1 = new Sighting();
        sighting1.setId(1L);
        Sighting sighting2 = new Sighting();
        sighting2.setId(sighting1.getId());
        assertThat(sighting1).isEqualTo(sighting2);
        sighting2.setId(2L);
        assertThat(sighting1).isNotEqualTo(sighting2);
        sighting1.setId(null);
        assertThat(sighting1).isNotEqualTo(sighting2);
    }
}
