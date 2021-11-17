package com.psl.miniProject.controller;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psl.miniProject.modal.Products;
import com.psl.miniProject.repository.AdminProductsRepository;
import com.psl.miniProject.modal.Ratings;
import com.psl.miniProject.repository.RatingsRepository;


@RestController
@RequestMapping("/admin")
public class AdminProductsController {



    @Autowired
    private RatingsRepository ratingsRepository;





    public static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/imgs";

    @Autowired
    private AdminProductsRepository adminProductsRepository;


    @GetMapping("/products")
    public List<Products> getAllProducts() {
        return (List<Products>) adminProductsRepository.findAll();
    }

    @PostMapping(value="/products",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Products createProduct(@RequestPart("product") String product,@RequestPart("file") MultipartFile file) {
        Products p1 = new Products();
        ObjectMapper objm = new ObjectMapper();
        try {
            p1 = objm.readValue(product,Products.class);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        Ratings rat = new Ratings(0,0,0,0,0);
        p1.setRatings(rat);
        Products pro = adminProductsRepository.save(p1);
        String filename = pro.getId() + ".jpg";
        try {
            Files.copy(file.getInputStream(), Paths.get(uploadDir,filename),StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pro.setProduct_photo(filename);
        adminProductsRepository.save(pro);
        ratingsRepository.save(rat);
        return pro;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Products> getProductsById(@PathVariable("id") int id) throws Exception {
        Products product = adminProductsRepository.
                           findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Products> updateProducts(@PathVariable("id") int id,
            @RequestBody Products productDetails) throws Exception {
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
    public ResponseEntity<Map<String,Boolean>> deleteProducts(@PathVariable("id") int id) throws Exception {
        Products product = adminProductsRepository.
                           findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
        adminProductsRepository.delete(product);
        Map<String,Boolean> map = new HashMap<>();
        map.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(map);
    }

}
