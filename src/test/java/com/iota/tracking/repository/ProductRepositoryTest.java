package com.iota.tracking.repository;


import com.iota.tracking.domain.Product;
import com.iota.tracking.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Client repository tests
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    // create Postgres container definition

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    private User user1, user2;
    private Product product1, product2;

    /**
     * Execute code before running each test
     */
    @BeforeEach
    public void setupModel() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        user1 = User.builder()
                .name("John Doe")
                .email("johndoe2@gmail.com")
                .password("password")
                .roles("ROLE_USER")
                .dateregistered(timestamp)
                .build();
        product1 = Product.builder()
                .id(1)
                .name("Product1")
                .quantity(2)
                .recipient("johndoe2@gmail.com")
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

        userRepository.save(user1);
        productRepository.save(product1);
    }

    /**
     * Execute code after running each test
     */
    @AfterEach
    public void tearDown() {
        product1 = null;
        product2 = null;
    }

    @Test
    @DisplayName("shouldFindByUserID")
    void findByUserID() {

        Optional<Product> product = productRepository.findById(product1.getId());
        assertThat(product).isNotEmpty();
      //  assertThat(user.get()).usingRecursiveComparison().ignoringFields("createddate","updateddate").isEqualTo(product1);
    }

    @Test
    @DisplayName("findAllByOrderByIdDesc")
    void findAllByOrderByIdDesc() {
        //productRepository.save(product1);
        List<Product> product = productRepository.findAllByOrderByIdDesc();
        assertThat(product).isNotEmpty();
    }

    @Test
    @DisplayName("findAllByRecipient")
    void findAllByRecipient() {
        //productRepository.save(product1);
        List<Product> product = productRepository.findAllByRecipient(product1.getRecipient());
        assertThat(product).isNotEmpty();
    }
}

