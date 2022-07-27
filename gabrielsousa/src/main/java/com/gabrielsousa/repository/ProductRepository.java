package com.gabrielsousa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielsousa.domain.Category;
import com.gabrielsousa.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	//TODO- Fazer o Query do repository funcionar
	@Transactional(readOnly=true)
//	Page<Product> findDistinctByNameContainingAndCategoriesIn(String name,List<Category>categories,Pageable pageRequest);
//	@Query(value = "SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cat WHERE obj.name LIKE %:name% AND cat IN :categories")
	Page<Product> findDistinctByNameContainingAndCategoriesIn(@Param("name")String name,@Param("categories")List<Category>categories,Pageable pageRequest);
}
