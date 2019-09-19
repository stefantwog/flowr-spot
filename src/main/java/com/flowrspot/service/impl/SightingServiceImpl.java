package com.flowrspot.service.impl;

import com.flowrspot.domain.User;
import com.flowrspot.repository.SightingLikeRepository;
import com.flowrspot.service.*;
import com.flowrspot.domain.Sighting;
import com.flowrspot.repository.SightingRepository;
import com.flowrspot.web.rest.vm.SightingVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Sighting.
 */
@Service
@Transactional
public class SightingServiceImpl implements SightingService {

    private final Logger log = LoggerFactory.getLogger(SightingServiceImpl.class);

    private final SightingRepository sightingRepository;

    private final SightingLikeService sightingLikeService;

    private final CloudinaryService cloudinaryService;

    private final FlowerService flowerService;

    private final QuoteService quoteService;

    public SightingServiceImpl(SightingRepository sightingRepository, SightingLikeService sightingLikeService, CloudinaryService cloudinaryService, FlowerService flowerService,
                               QuoteService quoteService) {
        this.sightingRepository = sightingRepository;
        this.sightingLikeService = sightingLikeService;
        this.cloudinaryService = cloudinaryService;
        this.flowerService = flowerService;
        this.quoteService = quoteService;
    }

    /**
     * Save a sighting.
     *
     * @param sighting the entity to save
     * @return the persisted entity
     */
    @Override
    public Sighting save(Sighting sighting) {
        log.debug("Request to save Sighting : {}", sighting);
        return sightingRepository.save(sighting);
    }

    /**
     * Get all the sightings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sighting> findAll(Pageable pageable) {
        log.debug("Request to get all Sightings");
        return sightingRepository.findAll(pageable);
    }

    /**
     * Get one sighting by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Sighting findOne(Long id) {
        log.debug("Request to get Sighting : {}", id);
        return sightingRepository.findOne(id);
    }

    /**
     * Delete the sighting by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sighting : {}", id);
        sightingRepository.delete(id);
    }

    @Override
    public List<Sighting> findByFlower(Long flowerId) {
        return sightingRepository.findByFlower_Id(flowerId);
    }

    @Override
    public void deleteByUser(Long id) {
        List<Sighting> sightings = sightingRepository.findByUser_Id(id);
        sightings.forEach(sighting -> sightingLikeService.deleteBySighting(sighting.getId()));
        sightingRepository.delete(sightings);
    }

    @Override
    public Sighting createSighting(SightingVM sightingVM, User user) {
        Sighting sighting = new Sighting();
        sighting.setLatitude(sightingVM.getLatitude());
        sighting.setLongitude(sightingVM.getLongitude());
        sighting.setUser(user);
        if(sightingVM.getImage()!=null) {
            sighting.setImage(cloudinaryService.uploadSightingPicture(sightingVM.getImage(), sightingVM.getImage().getName()));
        }
        if(sightingVM.getFlowerId()!=null) {
            sighting.setFlower(flowerService.findOne(sightingVM.getFlowerId()));
        }
        sighting.setQuote(quoteService.getQuoteOfTheDay());

        return sightingRepository.save(sighting);
    }
}
