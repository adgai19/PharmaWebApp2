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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.psl.miniProject.auth.SecurityService;
import com.psl.miniProject.auth.models.User;
import com.psl.miniProject.modal.Products;
import com.psl.miniProject.repository.AdminProductsRepository;
import com.psl.miniProject.modal.Ratings;
import com.psl.miniProject.repository.RatingsRepository;


@RestController
@RequestMapping("/admin")
public class AdminProductsController {


    @Autowired
    SecurityService securityService;

    @Autowired
    private RatingsRepository ratingsRepository;

    public static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/imgs";

    @Autowired
    private AdminProductsRepository adminProductsRepository;


    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        return idVerify(ResponseEntity.ok((List<Products>) adminProductsRepository.findAll()));

    }

    @PostMapping(value="/products",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createProduct(@RequestPart("product") String product,
                                        @RequestPart("file") MultipartFile file) {
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
        return idVerify(ResponseEntity.ok(pro));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProductsById(@PathVariable("id") int id) throws Exception {
        Products product = adminProductsRepository.
                           findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
        return idVerify(ResponseEntity.ok(product));

    }

    @PutMapping("/products/{id}")
    public ResponseEntity updateProducts(@PathVariable("id") int id,
                                         @RequestBody Products productDetails) throws Exception {
        Products product = adminProductsRepository.
                           findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
        product.setCategory(productDetails.getCategory());
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        Products updatedProduct = adminProductsRepository.save(product);
        return idVerify(ResponseEntity.ok(updatedProduct));

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProducts(@PathVariable("id") int id) throws Exception {
        Products product = adminProductsRepository.
                           findById(id).orElseThrow(() -> new Exception("Product not found with ID: "+id));
        adminProductsRepository.delete(product);
        Map<String,Boolean> map = new HashMap<>();
        map.put("deleted",Boolean.TRUE);
        return idVerify(ResponseEntity.ok(map));
    }

    // helper method for user auth.
    // bit hacky but will go with this for now
    private <T> ResponseEntity idVerify(ResponseEntity<T> identity) {
        // System.out.println("I am helper");
        // String email=securityService.getUser().getEmail();
        // if (email.contains("test@admin.com")) {
        return identity;
        // } else {
        // return ResponseEntity.badRequest().body("You are not an admin so cannot access this ");
        // }

    }
}
