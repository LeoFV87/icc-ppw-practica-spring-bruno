package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);

    List<ProductEntity> findByOwnerId(Long userId);

    List<ProductEntity> findByCategoriesId(Long categoryId);

    List<ProductEntity> findByCategoriesName(String categoryName);

    long countByCategoriesId(Long categoryId);

    @Query("SELECT p FROM ProductEntity p JOIN p.categories c WHERE c.id = :categoryId AND p.price > :price")
    List<ProductEntity> findByCategoryIdAndPriceAbove(@Param("categoryId") Long categoryId, @Param("price") Double price);
}