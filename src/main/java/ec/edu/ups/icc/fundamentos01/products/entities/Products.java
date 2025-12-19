package ec.edu.ups.icc.fundamentos01.products.entities;

public class Products {
    private int id;
    private String name;
    private Double price;
    private Integer stock;
    private String createdAt;
    public Products(int id, String name, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}