package com.flowrspot.repository;

import com.flowrspot.domain.Sighting;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Sighting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SightingRepository extends JpaRepository<Sighting, Long>, JpaSpecificationExecutor<Sighting> {

    List<Sighting> findByFlower_Id(Long flowerId);

    List<Sighting> findByUser_Id(Long flowerId);

    void deleteByUser_Id(Long id);
}
