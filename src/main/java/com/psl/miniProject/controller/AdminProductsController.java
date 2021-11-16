package com.psl.miniProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psl.miniProject.modal.Products;
import com.psl.miniProject.modal.Ratings;
import com.psl.miniProject.modal.Users;
import com.psl.miniProject.repository.AdminProductsRepository;
import com.psl.miniProject.repository.RatingsRepository;
import com.psl.miniProject.repository.UsersRepository;


@RestController
@RequestMapping("/admin")
public class AdminProductsController {
	@Autowired
	private AdminProductsRepository adminProductsRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private RatingsRepository ratingsRepository;
	
	@GetMapping("/products")
	public List<Products> getAllProducts(){
		return (List<Products>) adminProductsRepository.findAll();
	}
	
	@PostMapping("/products")
	public Products createProduct(@RequestBody Products product) {
		Products p = new Products();
		Ratings rat = new Ratings(0,0,0,0,0);
		p.setCategory(product.getCategory());
		p.setName(product.getName());
		p.setPrice(product.getPrice());
		p.setStock(product.getStock());
		p.setDescription(product.getDescription());
		p.setRatings(rat);
		Products pro = adminProductsRepository.save(p);
		ratingsRepository.save(rat);
		return pro;
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Products> getProductsById(@PathVariable("id") int id) throws Exception{
		Products product = adminProductsRepository.
				findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
		return ResponseEntity.ok(product);
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Products> updateProducts(@PathVariable("id") int id,@RequestBody Products productDetails) throws Exception{
		Products product = adminProductsRepository.
				findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
		product.setCategory(productDetails.getCategory());
		product.setName(productDetails.getName());
		product.setPrice(productDetails.getPrice());
		product.setStock(productDetails.getStock());
		Products updatedProduct = adminProductsRepository.save(product);
		return ResponseEntity.ok(updatedProduct);
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Map<String,Boolean>> deleteProducts(@PathVariable("id") int id) throws Exception{
		Products product = adminProductsRepository.
				findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
		adminProductsRepository.delete(product);
		Map<String,Boolean> map = new HashMap<>();
		map.put("deleted",Boolean.TRUE);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<Users>> getAllUsers(){
		List<Users> users = (List<Users>) usersRepository.findAll();
		return ResponseEntity.ok(users);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Map<String,Boolean>> deleteUsers(@PathVariable("id") int id) throws Exception{
		Users user = usersRepository.
				findById(id).orElseThrow(() -> new Exception("User not found with ID: "+id));
		usersRepository.delete(user);
		Map<String,Boolean> map = new HashMap<>();
		map.put("deleted",Boolean.TRUE);
		return ResponseEntity.ok(map);
	}

	

}
