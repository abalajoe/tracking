package com.iota.tracking.serviceimpl;

import com.iota.tracking.domain.Event;
import com.iota.tracking.domain.Product;
import com.iota.tracking.dto.ProductDTO;
import com.iota.tracking.exception.EntityNotFoundException;
import com.iota.tracking.repository.EventRepository;
import com.iota.tracking.repository.ProductRepository;
import com.iota.tracking.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<Product> findAllProductsByRecipient(String email) {
        return productRepository.findAllByRecipient(email);
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Product product = Product.builder().
                name(productDTO.getName()).
                quantity(productDTO.getQuantity()).
                cost(productDTO.getCost()).
                recipient(productDTO.getRecipient()).
                deliveryaddress(productDTO.getDeliveryAddress()).
                custodian(productDTO.getCustodian()).
                location(productDTO.getLocation()).
                createddate(new Timestamp(System.currentTimeMillis())).
                updateddate(new Timestamp(System.currentTimeMillis())).
                build();

        Product p = productRepository.save(product);
        Event event = Event.builder()
                .productfk(p.getId())
                .custodian(productDTO.getCustodian())
                .location(productDTO.getLocation())
                .createddate(new Timestamp(System.currentTimeMillis()))
                .build();
        eventRepository.save(event);
        return p;
    }

    @Override
    public Product edit(int id, ProductDTO productDTO) {
        Product p = productRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Resource Not Found"));

        p.setName(productDTO.getName());
        p.setQuantity(productDTO.getQuantity());
        p.setCost(productDTO.getCost());
        p.setCustodian(productDTO.getCustodian());
        p.setLocation(productDTO.getLocation());
        p.setDeliveryaddress(productDTO.getDeliveryAddress());
        return productRepository.save(p);
    }

    @Override
    public Product event(int id, String custodian, String location) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Resource Not Found"));

        Event event = Event.builder()
                .productfk(product.getId())
                .custodian(custodian)
                .location(location)
                .createddate(new Timestamp(System.currentTimeMillis()))
                .build();
        eventRepository.save(event);

        product.setCustodian(custodian);
        product.setLocation(location);
        product.setUpdateddate(new Timestamp(System.currentTimeMillis()));

        return productRepository.save(product);
    }


}
