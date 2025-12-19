package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.Products;

public class ProductsMapper {
    public static Products toEntity(int id, String name, Double price, Integer stock) {
        return new Products(id, name, price, stock);
    }

    public static ProductsResponseDto toResponse(Products product) {
        ProductsResponseDto dto = new ProductsResponseDto();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.stock = product.getStock();
        return dto;
    }
}
