package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByOnSaleTrue();

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.category c
            LEFT JOIN p.group g
            WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:categoryId IS NULL OR c.id = :categoryId)
            AND (:groupId IS NULL OR g.id = :groupId)
            AND (:minPrice IS NULL OR p.price >= :minPrice)
            AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    List<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("groupId") Integer groupId,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 Sort sort);
}