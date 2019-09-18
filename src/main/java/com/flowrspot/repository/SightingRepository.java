package com.flowrspot.repository;

import com.flowrspot.domain.Sighting;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sighting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SightingRepository extends JpaRepository<Sighting, Long>, JpaSpecificationExecutor<Sighting> {

}
