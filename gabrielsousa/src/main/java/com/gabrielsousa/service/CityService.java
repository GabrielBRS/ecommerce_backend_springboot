package com.gabrielsousa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielsousa.domain.City;
import com.gabrielsousa.repository.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;
	
//  TODO-FAZER O MÉTODO FindCities funcionar
	public List<City> findByState(Integer stateId){
		return cityRepository.findSelectByState(stateId);
	}
}
