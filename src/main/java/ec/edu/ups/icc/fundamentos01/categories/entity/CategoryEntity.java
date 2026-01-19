package ec.edu.ups.icc.fundamentos01.categories.entity;

import ec.edu.ups.icc.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseModel {

    @Column(nullable=false, length=100, unique= true)
    private String name;

    @Column(length=500)
    private String description;

    @ManyToOne(optional= false, fetch= FetchType.LAZY)
    @JoinColumn(name="user_id", nullable= false)
    private UserEntity owner;

    // Recursividad (Categoría padre)
    @ManyToOne(optional= false, fetch= FetchType.LAZY)
    @JoinColumn(name="category_id", nullable= false)
    private CategoryEntity category;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public CategoryEntity() {}

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UserEntity getOwner() { return owner; }
    public void setOwner(UserEntity owner) { this.owner = owner; }
    public CategoryEntity getCategory() { return category; }
    public void setCategory(CategoryEntity category) { this.category = category; }
    
    // Getter/Setter para productos
    public Set<ProductEntity> getProducts() { return products; }
    public void setProducts(Set<ProductEntity> products) { this.products = products; }
}