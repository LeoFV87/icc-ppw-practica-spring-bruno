package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Products;
import java.util.stream.Collectors; // Import necesario

public class ProductsMapper {

    public static ProductsResponseDto toResponse(ProductEntity entity) {
        if (entity == null) return null;
        
        ProductsResponseDto dto = new ProductsResponseDto();
        
        if (entity.getId() != null) dto.setId(entity.getId().intValue());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().toString());
        }

        if (entity.getOwner() != null) {
            ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
            userDto.id = entity.getOwner().getId();
            userDto.name = entity.getOwner().getName();
            userDto.email = entity.getOwner().getEmail();
            dto.user = userDto;
        }

        // === CAMBIO CLAVE: Mapeo de Lista de Categorías (N:N) ===
        if (entity.getCategories() != null) {
            dto.categories = entity.getCategories().stream()
                .map(cat -> {
                    ProductsResponseDto.CategoryResponseDto catDto = new ProductsResponseDto.CategoryResponseDto();
                    catDto.id = cat.getId();
                    catDto.name = cat.getName();
                    catDto.description = cat.getDescription();
                    return catDto;
                })
                .collect(Collectors.toList());
        }

        return dto;
    }

    public static ProductsResponseDto toResponse(Products product) {
        if (product == null) return null;
        ProductsResponseDto dto = new ProductsResponseDto();
        if (product.getId() != null) dto.setId(product.getId().intValue());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
}