package com.iota.tracking.service;


import com.iota.tracking.domain.Product;
import com.iota.tracking.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<Product> findAllProducts();
    List<Product> findAllProductsByRecipient(String email);
    Product create(ProductDTO productDTO);

    Product edit(int id, ProductDTO productDTO);
    Product event(int id, String custodian, String location);
}
