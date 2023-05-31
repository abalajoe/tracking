package com.iota.tracking.api;

import com.iota.tracking.domain.Product;
import com.iota.tracking.dto.ProductDTO;
import com.iota.tracking.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Product Controller API Endpoint
 */
@Slf4j
@RestController
@RequestMapping({"/api/iota/tracking/v1/product"})
public class ProductResource {
    @Autowired
    IProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        log.info("createProduct {} ", productDTO);
        return ResponseEntity.ok(productService.create(productDTO));
    }

    @PutMapping("/edit")
    public ResponseEntity<Product> editProduct(@RequestParam("id") int id,
                                               @RequestBody @Valid ProductDTO productDTO) {
        log.info("editProduct {} {} ", id, productDTO);
        return ResponseEntity.ok(productService.edit(id, productDTO));
    }

    @GetMapping({"/findAllProducts"})
    public ResponseEntity<List<Product>> findAllProducts() {
        log.info("findAllProducts");
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping({"/findAllProductsByRecipient"})
    public ResponseEntity<List<Product>> findAllProductsByRecipient(@RequestParam("email") String email) {
        log.info("findAllProductsByRecipient {}", email);
        return ResponseEntity.ok(productService.findAllProductsByRecipient(email));
    }

    @PutMapping("/event")
    public ResponseEntity<Product> event(
            @RequestParam("id") int id,
            @RequestParam("custodian") String custodian,
            @RequestParam("location") String location) {
        log.info("postevent {} {} {} ", id, custodian, location);
        return ResponseEntity.ok(productService.event(id, custodian, location));
    }
}
