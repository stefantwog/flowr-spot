package com.flowrspot.service.impl;

import com.flowrspot.service.CloudinaryService;
import com.flowrspot.service.FlowerService;
import com.flowrspot.domain.Flower;
import com.flowrspot.repository.FlowerRepository;
import com.flowrspot.web.rest.vm.FlowerVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Flower.
 */
@Service
@Transactional
public class FlowerServiceImpl implements FlowerService {

    private final Logger log = LoggerFactory.getLogger(FlowerServiceImpl.class);

    private final FlowerRepository flowerRepository;

    private final CloudinaryService cloudinaryService;

    public FlowerServiceImpl(FlowerRepository flowerRepository, CloudinaryService cloudinaryService) {
        this.flowerRepository = flowerRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /**
     * Save a flower.
     *
     * @param flower the entity to save
     * @return the persisted entity
     */
    @Override
    public Flower save(Flower flower) {
        log.debug("Request to save Flower : {}", flower);
        return flowerRepository.save(flower);
    }

    /**
     * Get all the flowers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Flower> findAll(Pageable pageable) {
        log.debug("Request to get all Flowers");
        return flowerRepository.findAll(pageable);
    }

    /**
     * Get one flower by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Flower findOne(Long id) {
        log.debug("Request to get Flower : {}", id);
        return flowerRepository.findOne(id);
    }

    /**
     * Delete the flower by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Flower : {}", id);
        flowerRepository.delete(id);
    }

    @Override
    public Flower createFlower(FlowerVM flowerVM) {
        Flower flower = new Flower();
        flower.setDescription(flowerVM.getDescription());
        flower.setName(flowerVM.getName());
        if(flowerVM.getImage()!=null) {
            flower.setImage(cloudinaryService.uploadSightingPicture(flowerVM.getImage(), flowerVM.getImage().getName()));
        }

        return flowerRepository.save(flower);
    }
}
