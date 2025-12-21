package com.sparta.msa.project_part_3.domain.product.repository;

import com.sparta.msa.project_part_3.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {

  @Query("SELECT p FROM Product p JOIN FETCH p.category")
  Page<Product> findAllWithCategory(Pageable categoryId);

  Optional<Product> findByExternalProductId(Long externalProductId);

}
