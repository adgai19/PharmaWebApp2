package com.psl.miniProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.psl.miniProject.modal.Products;

public interface ProductsRepository extends CrudRepository<Products,Integer>{
	@Query("SELECT DISTINCT a.category FROM Products a")
	public List<String> findAllCategory();
	
	public List<Products> findByCategory(String category_name);
	
	public List<Products> findByNameContaining(String keyword);

}
