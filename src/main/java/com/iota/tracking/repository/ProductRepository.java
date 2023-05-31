package com.iota.tracking.repository;

import com.iota.tracking.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(int id);
    List<Product> findAllByOrderByIdDesc();
    List<Product> findAllByRecipient(String email);
}
