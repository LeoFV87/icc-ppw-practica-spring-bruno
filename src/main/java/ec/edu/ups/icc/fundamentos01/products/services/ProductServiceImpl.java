package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class ProductServiceImpl implements ProductsService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    // Helper para buscar múltiples categorías a la vez
    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {
        Set<CategoryEntity> categories = new HashSet<>();
        if (categoryIds != null) {
            for (Long catId : categoryIds) {
                CategoryEntity category = categoryRepo.findById(catId)
                        .orElseThrow(() -> new NotFoundException("Categoría no encontrada ID: " + catId));
                categories.add(category);
            }
        }
        return categories;
    }

    @Override
    public ProductsResponseDto create(CreateProductsDto dto) {
        // 1. Validar Dueño
        UserEntity owner = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // 2. Validar Categorías (N:N)
        Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());

        // 3. Regla Negocio
        if (productRepo.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("Nombre duplicado");
        }

        // 4. Crear
        ProductEntity entity = new ProductEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock() != null ? dto.getStock() : 0);
        entity.setOwner(owner);
        entity.setCategories(categories); // Asignamos colección

        // 5. Persistir
        return toResponseDto(productRepo.save(entity));
    }

    @Override
    public Object update(int id, UpdateProductsDto dto) {
        ProductEntity existing = productRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // Actualizar campos simples
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getStock() != null) existing.setStock(dto.getStock());

        // Actualizar Categorías (N:N) si vienen en el DTO
        //
        if (dto.getCategoryIds() != null) {
            Set<CategoryEntity> newCategories = validateAndGetCategories(dto.getCategoryIds());
            existing.clearCategories(); // Borrar anteriores
            existing.setCategories(newCategories); // Poner nuevas
        }

        return toResponseDto(productRepo.save(existing));
    }

    // Convertidor actualizado
    private ProductsResponseDto toResponseDto(ProductEntity entity) {
        ProductsResponseDto dto = new ProductsResponseDto();
        if (entity.getId() != null) dto.setId(entity.getId().intValue());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        
        // Owner
        if (entity.getOwner() != null) {
            ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
            userDto.id = entity.getOwner().getId();
            userDto.name = entity.getOwner().getName();
            userDto.email = entity.getOwner().getEmail();
            dto.user = userDto;
        }

        // Categorías (Lista)
        if (entity.getCategories() != null) {
            dto.categories = entity.getCategories().stream()
                .map(cat -> {
                    ProductsResponseDto.CategoryResponseDto catDto = new ProductsResponseDto.CategoryResponseDto();
                    catDto.id = cat.getId();
                    catDto.name = cat.getName();
                    catDto.description = cat.getDescription();
                    return catDto;
                }).toList();
        }
        return dto;
    }

    @Override public List<ProductsResponseDto> findAll() {
        return productRepo.findAll().stream().map(this::toResponseDto).toList();
    }
    @Override public Object findOne(int id) {
        return productRepo.findById((long) id).map(this::toResponseDto).orElseThrow(() -> new NotFoundException("No encontrado"));
    }
   
    @Override public Object delete(int id) { productRepo.deleteById((long)id); return java.util.Map.of("ok", true); }
    @Override public Object partialUpdate(int id, PartialUpdateProductsDto dto) { return null; }
    @Override public List<ProductsResponseDto> findByUserId(Long id) { return productRepo.findByOwnerId(id).stream().map(this::toResponseDto).toList(); }
    @Override public List<ProductsResponseDto> findByCategoryId(Long id) { return List.of(); } 

    @Override
    public Map<String, Object> countProductsByCategory(Long categoryId) {
    if (!categoryRepo.existsById(categoryId)) {
        throw new NotFoundException("Categoría no encontrada");
    }
    long count = productRepo.countByCategoriesId(categoryId);
    return Map.of(
        "categoryId", categoryId,
        "productCount", count
    );
}

}