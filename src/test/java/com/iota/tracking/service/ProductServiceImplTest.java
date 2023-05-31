package com.iota.tracking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iota.tracking.domain.Product;
import com.iota.tracking.dto.ProductDTO;
import com.iota.tracking.exception.EntityNotFoundException;
import com.iota.tracking.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Autowired
    private IProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Product product1, product2;
    private ProductDTO productDTO;

    @BeforeEach
    public void setupModel() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        product1 = Product.builder()
                .id(1)
                .name("Product1")
                .quantity(2)
                .recipient("joeabala")
                .deliveryaddress("Ngong")
                .cost(3000)
                .custodian("USP")
                .location("Nairobi")
                .build();

        product2 = Product.builder()
                .id(1)
                .name("Product2")
                .quantity(3)
                .recipient("mary")
                .deliveryaddress("Nairobi")
                .cost(4000)
                .custodian("USP")
                .location("Kisumu")
                .build();

        productDTO = ProductDTO.builder()
                .name("Product1")
                .quantity(2)
                .cost(3400)
                .recipient("johndoe")
                .custodian("UPO")
                .location("Naivasha")
                .deliveryAddress("Nakuru")
                .build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("shouldCreateProduct")
    public void shouldCreateProduct() {
        when(productRepository.save(any())).thenReturn(product1);

        // when
        productService.create(productDTO);

        // then
        // capture user value inserted
        ArgumentCaptor<Product> userArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(userArgumentCaptor.capture());
        Product capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(product1.getName());
    }

    @Test
    @DisplayName("shouldUpdateProduct")
    public void shouldUpdateProduct() {
        // create mock behaviour
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product1));

        // execute service call
        productService.edit(product1.getId(), productDTO);

        // verify
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("shouldUpdateProductThrowResourceNotFound")
    public void shouldUpdateProductThrowResourceNotFound() {
        // create mock behaviour
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product1));
        // execute service call
        assertThatThrownBy(() -> productService.edit(product2.getId(), productDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource Not Found");
        // verify
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("shouldPostProductEvent")
    public void shouldPostProductEvent() {
        // create mock behaviour
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product1));

        // execute service call
        productService.event(product1.getId(), product1.getCustodian(), product1.getLocation());

        // verify
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("shouldPostProductEventThrowResourceNotFound")
    public void shouldPostProductEventThrowResourceNotFound() {
        // create mock behaviour
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product1));
        // execute service call
        assertThatThrownBy(() -> productService.event(product2.getId(),
                product2.getCustodian(), product2.getLocation()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource Not Found");
        // verify
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("shouldFindAllProducts")
    public void shouldFindAllProducts(){
        // create mock behaviour
        when(productRepository.findAllByOrderByIdDesc()).thenReturn(Arrays.asList(product1, product2));

        // Execute service call
        List<Product> usersList = productService.findAllProducts();

        // assert
        assertEquals(2, usersList.size());
        verify(productRepository, times(1)).findAllByOrderByIdDesc();
    }

    @Test
    @DisplayName("shouldFindProductsByRecipient")
    public void shouldFindProductsByRecipient(){
        // create mock behaviour
        when(productRepository.findAllByRecipient("user@gmail.com")).thenReturn(Arrays.asList(product1, product2));

        // Execute service call
        List<Product> usersList = productService.findAllProductsByRecipient("user@gmail.com");

        // assert
        assertEquals(2, usersList.size());
        verify(productRepository, times(1)).findAllByRecipient("user@gmail.com");
    }
}
