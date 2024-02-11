package com.sivaranjani.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sivaranjani.model.Product;
import com.sivaranjani.repository.ProductRepo;

//import java.util.Optional;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductRepo productRepo; //repository object
	
	private static final double VALUE = 100;

	@GetMapping("/getAllProducts")
	public ResponseEntity<List<Product>> getAllProducts() {//READ /get/retrieve

		try {
			List<Product> productList = new ArrayList<>();
			productRepo.findAll().forEach(productList::add);

			if (productList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(productList, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getProductById/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {//READ /get/retrieve

		Optional<Product> productObj = productRepo.findById(id); //java 8 -> optional features..data could be null or present
		if (productObj.isPresent()) {
			return new ResponseEntity<>(productObj.get(), HttpStatus.OK);
		} 
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);	

	}


	@PostMapping("/addProduct")
	public ResponseEntity<Product> addProduct(@RequestBody Product product){ //CREATE /save/post

		try {
			Product productObj = productRepo.save(product);
			return new ResponseEntity<>(productObj, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	 @PostMapping("/updateProductById/{id}")
	    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody Product product) {
	        try {
	            Optional<Product> productData = productRepo.findById(id);
	            if (productData.isPresent()) {
	                Product updatedProductData = productData.get();
	                updatedProductData.setName(product.getName());
	                updatedProductData.setDescription(product.getDescription());

	                Product productObj = productRepo.save(updatedProductData);
	                return new ResponseEntity<>(productObj, HttpStatus.CREATED);
	            }

	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 
	 @PostMapping("/updateProductPriceById/{id}")
	    public ResponseEntity<Product> updateProductPriceById(@PathVariable Long id, @RequestParam String name, @RequestParam int percentageValue) {
	        try {
	            Optional<Product> productData = productRepo.findById(id);
	            if (productData.isPresent()) {
	            	Product updatedProductData = productData.get();
	                double originalprice = updatedProductData.getPrice();
	                if(name.equalsIgnoreCase("discount")) {
	                	 updatedProductData.setPrice(originalprice - (percentageValue * originalprice/VALUE));
	                }else if(name.equalsIgnoreCase("tax")) {
	                	updatedProductData.setPrice(originalprice + (percentageValue * originalprice/VALUE));
	                }
	    
	                Product productObj = productRepo.save(updatedProductData);
	                return new ResponseEntity<>(productObj, HttpStatus.OK);
	            }

	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }


	@DeleteMapping("/deleteProductById/{id}")
	public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Long id) {//DELETE
		try {
			productRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/deleteAllProducts")
	public ResponseEntity<HttpStatus> deleteAllProducts() {
		try {
			productRepo.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
