package com.iota.tracking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iota.tracking.domain.Product;
import com.iota.tracking.dto.ProductDTO;
import com.iota.tracking.service.IProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private static Product product1, product2;
    private static ProductDTO productDTO;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * Execute code before running each test
     */
    @BeforeAll
    public static void setupModel() {
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
                .createddate(timestamp)
                .updateddate(timestamp)
                .build();

        product2 = Product.builder()
                .id(1)
                .name("Product2")
                .quantity(4)
                .recipient("marynjunguna")
                .deliveryaddress("Karen")
                .cost(4000)
                .custodian("UDP")
                .location("Kisumu")
                .createddate(timestamp)
                .updateddate(timestamp)
                .build();

        productDTO = ProductDTO.builder()
                .name("Product10")
                .quantity(2)
                .cost(3400)
                .recipient("johndoe")
                .custodian("UPO")
                .location("Naivasha")
                .deliveryAddress("Nakuru")
                .build();

    }

    /**
     * Execute code after running each test
     */
    @AfterEach
    public void tearDown() {

    }

    @Test
    @DisplayName("shouldForbidCreateUserIfRoleUser")
    public void shouldForbidCreateUserIfRoleUser() throws Exception {
        mockMvc.perform(post("/api/iota/tracking/v1/product/create")
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("USER"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("shouldForbidEditProductIfRoleUser")
    public void shouldForbidEditProductIfRoleUser() throws Exception {
        mockMvc.perform(put("/api/iota/tracking/v1/product/edit/{userId}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("USER"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("shouldForbidFindAllProductsIfRoleUser")
    public void shouldForbidFindAllProductsIfRoleUser() throws Exception {
        mockMvc.perform(get("/api/iota/tracking/v1/product/findAllProducts")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("shouldForbidPostEventIfRoleUser")
    public void shouldForbidPostEventIfRoleUser() throws Exception {
        mockMvc.perform(put("/api/iota/tracking/v1/product/event")
                        .param("id", "1")
                        .param("custodian", "joeabala")
                        .param("location", "nairobi")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("shouldAllowFindAllProductsIfRoleAdmin")
    public void shouldAllowFindAllProductsIfRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/iota/tracking/v1/product/findAllProducts")
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("shouldAllowFindAllProductsByRecipientIfRoleUser")
    public void shouldAllowFindAllProductsByRecipientIfRoleUser() throws Exception {
        mockMvc.perform(get("/api/iota/tracking/v1/product/findAllProductsByRecipient")
                        .param("email", "user@gmail.com")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("shouldAllowFindAllProductsByRecipientIfRoleAdmin")
    public void shouldAllowFindAllProductsByRecipientIfRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/iota/tracking/v1/product/findAllProductsByRecipient")
                        .param("email", "admin@gmail.com")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    @DisplayName("shouldAllowCreateProductIfRoleAdmin")
    public void shouldAllowCreateProductIfRoleAdmin() throws Exception {
        when(productService.create(any(ProductDTO.class))).thenReturn(product1);
        mockMvc.perform(post("/api/iota/tracking/v1/product/create")
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    @DisplayName("shouldAllowEditProductIfRoleAdmin")
    public void shouldAllowEditProductIfRoleAdmin() throws Exception {
        when(productService.create(any(ProductDTO.class))).thenReturn(product1);
        mockMvc.perform(put("/api/iota/tracking/v1/product/edit")
                        .param("id", "1")
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("shouldAllowPostEventIfRoleAdmin")
    public void shouldAllowPostEventIfRoleAdmin() throws Exception {
        mockMvc.perform(put("/api/iota/tracking/v1/product/event")
                        .param("id", "1")
                        .param("custodian", "joeabala")
                        .param("location", "nairobi")
                        .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
