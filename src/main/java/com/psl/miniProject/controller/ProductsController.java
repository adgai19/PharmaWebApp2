package com.psl.miniProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psl.miniProject.modal.Products;
import com.psl.miniProject.repository.ProductsRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/products")
public class ProductsController {
	
	@Autowired
	private ProductsRepository productsRepository;
	
	//get all products
	@GetMapping("/")
	public List<Products> getAllProducts(){
		return (List<Products>)productsRepository.findAll();
	}
	
	//get all categories name
	@GetMapping("/categories")
	public List<String> getAllCategory(){
		return (List<String>)productsRepository.findAllCategory();
	}
	
	//get all products by category
	@GetMapping("/category/{name}")
	public List<Products> getByCategory(@PathVariable("name") String name){
		return (List<Products>)productsRepository.findByCategory(name);
	}
	
	//get products by ID
	@GetMapping("/{id}")
	public Optional<Products> getById(@PathVariable("id") int id) {
		return productsRepository.findById(id);
	}
	
	//search products
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<Products>> search(@PathVariable("keyword") String keyword){
		List<Products> pro = productsRepository.findByNameContaining(keyword);
		return ResponseEntity.ok(pro);
	}
	
	@PutMapping("/checkout")
	public ResponseEntity<String> updateProductsStock(@RequestBody HashMap<String,Integer> productDetails) throws Exception{
		for(Map.Entry<String,Integer> entry : productDetails.entrySet()) {
			Products product = productsRepository.findById(Integer.parseInt(entry.getKey()))
					.orElseThrow(() -> new Exception("Product not found with ID: "+entry.getKey()));
			product.setStock(product.getStock() - entry.getValue());
			productsRepository.save(product);
		}
		return ResponseEntity.ok("Stocks updated");
	}

}
