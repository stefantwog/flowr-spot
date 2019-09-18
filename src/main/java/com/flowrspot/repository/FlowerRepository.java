package com.flowrspot.repository;

import com.flowrspot.domain.Flower;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Flower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long>, JpaSpecificationExecutor<Flower> {

}
