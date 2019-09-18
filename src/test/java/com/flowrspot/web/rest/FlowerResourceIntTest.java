package com.flowrspot.web.rest;

import com.flowrspot.FlowrSpotApp;

import com.flowrspot.domain.Flower;
import com.flowrspot.domain.Sighting;
import com.flowrspot.repository.FlowerRepository;
import com.flowrspot.service.FlowerService;
import com.flowrspot.web.rest.errors.ExceptionTranslator;
import com.flowrspot.service.dto.FlowerCriteria;
import com.flowrspot.service.FlowerQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.flowrspot.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FlowerResource REST controller.
 *
 * @see FlowerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlowrSpotApp.class)
public class FlowerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private FlowerService flowerService;

    @Autowired
    private FlowerQueryService flowerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFlowerMockMvc;

    private Flower flower;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FlowerResource flowerResource = new FlowerResource(flowerService, flowerQueryService);
        this.restFlowerMockMvc = MockMvcBuilders.standaloneSetup(flowerResource)
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
    public static Flower createEntity(EntityManager em) {
        Flower flower = new Flower()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .description(DEFAULT_DESCRIPTION);
        return flower;
    }

    @Before
    public void initTest() {
        flower = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlower() throws Exception {
        int databaseSizeBeforeCreate = flowerRepository.findAll().size();

        // Create the Flower
        restFlowerMockMvc.perform(post("/api/flowers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isCreated());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate + 1);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFlower.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFlower.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createFlowerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flowerRepository.findAll().size();

        // Create the Flower with an existing ID
        flower.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowerMockMvc.perform(post("/api/flowers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFlowers() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList
        restFlowerMockMvc.perform(get("/api/flowers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flower.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getFlower() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get the flower
        restFlowerMockMvc.perform(get("/api/flowers/{id}", flower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flower.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllFlowersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where name equals to DEFAULT_NAME
        defaultFlowerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the flowerList where name equals to UPDATED_NAME
        defaultFlowerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFlowersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFlowerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the flowerList where name equals to UPDATED_NAME
        defaultFlowerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFlowersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where name is not null
        defaultFlowerShouldBeFound("name.specified=true");

        // Get all the flowerList where name is null
        defaultFlowerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllFlowersByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where image equals to DEFAULT_IMAGE
        defaultFlowerShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the flowerList where image equals to UPDATED_IMAGE
        defaultFlowerShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllFlowersByImageIsInShouldWork() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultFlowerShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the flowerList where image equals to UPDATED_IMAGE
        defaultFlowerShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllFlowersByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        flowerRepository.saveAndFlush(flower);

        // Get all the flowerList where image is not null
        defaultFlowerShouldBeFound("image.specified=true");

        // Get all the flowerList where image is null
        defaultFlowerShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    public void getAllFlowersBySightingsIsEqualToSomething() throws Exception {
        // Initialize the database
        Sighting sightings = SightingResourceIntTest.createEntity(em);
        em.persist(sightings);
        em.flush();
        flowerRepository.saveAndFlush(flower);
        Long sightingsId = sightings.getId();

        // Get all the flowerList where sightings equals to sightingsId
        defaultFlowerShouldBeFound("sightingsId.equals=" + sightingsId);

        // Get all the flowerList where sightings equals to sightingsId + 1
        defaultFlowerShouldNotBeFound("sightingsId.equals=" + (sightingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFlowerShouldBeFound(String filter) throws Exception {
        restFlowerMockMvc.perform(get("/api/flowers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flower.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFlowerShouldNotBeFound(String filter) throws Exception {
        restFlowerMockMvc.perform(get("/api/flowers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFlower() throws Exception {
        // Get the flower
        restFlowerMockMvc.perform(get("/api/flowers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlower() throws Exception {
        // Initialize the database
        flowerService.save(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower
        Flower updatedFlower = flowerRepository.findOne(flower.getId());
        // Disconnect from session so that the updates on updatedFlower are not directly saved in db
        em.detach(updatedFlower);
        updatedFlower
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION);

        restFlowerMockMvc.perform(put("/api/flowers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlower)))
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Create the Flower

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFlowerMockMvc.perform(put("/api/flowers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flower)))
            .andExpect(status().isCreated());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFlower() throws Exception {
        // Initialize the database
        flowerService.save(flower);

        int databaseSizeBeforeDelete = flowerRepository.findAll().size();

        // Get the flower
        restFlowerMockMvc.perform(delete("/api/flowers/{id}", flower.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flower.class);
        Flower flower1 = new Flower();
        flower1.setId(1L);
        Flower flower2 = new Flower();
        flower2.setId(flower1.getId());
        assertThat(flower1).isEqualTo(flower2);
        flower2.setId(2L);
        assertThat(flower1).isNotEqualTo(flower2);
        flower1.setId(null);
        assertThat(flower1).isNotEqualTo(flower2);
    }
}
