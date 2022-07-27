package com.gabrielsousa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielsousa.domain.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	
	@Transactional(readOnly=true)
//  TODO-FAZER O MÉTODO FindCities funcionar
//	@Query("SELECT obj FROM City obj WHERE obj.state.id = :stateId ORDER BY obj.name")
//	public List<City> state(@Param("stateId") Integer state_id);

//	Query model findDistinctByNameContainingAndCategoriesIn
	public List<City> findSelectByState(@Param("stateId") Integer state_id);
	
}
