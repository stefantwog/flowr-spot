package com.flowrspot.repository;

import com.flowrspot.domain.SightingLike;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SightingLike entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SightingLikeRepository extends JpaRepository<SightingLike, Long>, JpaSpecificationExecutor<SightingLike> {

}
