package com.gabrielsousa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gabrielsousa.domain.Category;
import com.gabrielsousa.dto.CategoryDTO;
import com.gabrielsousa.repository.CategoryRepository;
import com.gabrielsousa.service.exception.DataIntegrityException;
import com.gabrielsousa.service.exception.ObjectNotFoundException;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	public Category find(Integer id) {
		Optional<Category> obj = categoryRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: "
				+ Category.class.getName()));
	}
	
	public List<Category> findAll(){
		return categoryRepository.findAll();
	}
	
	public Page<Category> findPage(Integer page, Integer linesPerPage, 
			String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, 
				Direction.valueOf(direction), orderBy);
		return categoryRepository.findAll(pageRequest);
	}
	
	public Category insert(Category obj) {
		obj.setId(null);
		return categoryRepository.save(obj);
	}
	
	public Category update(Category obj) {
		Category newObj = find(obj.getId());
		updateData(newObj,obj);
		return categoryRepository.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			categoryRepository.deleteById(id);
		}catch(DataIntegrityException e) {
			throw new DataIntegrityException("Não é possível "
					+ "excluir uma categoria que possui produtos!");
		}
	}
	
	public Category fromDTO(CategoryDTO objDTO) {
		return new Category(objDTO.getId(),objDTO.getName());
	}
	
	private void updateData(Category newObj, Category obj) {
		newObj.setName(obj.getName());
	}
}
