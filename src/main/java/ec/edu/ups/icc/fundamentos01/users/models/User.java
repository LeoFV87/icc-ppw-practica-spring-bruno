package ec.edu.ups.icc.fundamentos01.users.models;

import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String createdAt;

    public User(int id, String name, String email, String password) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nombre inválido");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email inválido");
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password inválido");

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Constructor parametrizado para lógica interna
    public User(int id, String name, String email, String password, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Crea un User desde un DTO de creación.
     */
    public static User fromDto(CreateUserDto dto) {
        return new User(0, dto.getName(), dto.getEmail(), dto.getPassword());
    }

    /**
     * Crea un User desde una entidad persistente recuperada de la BD.
     */
    public static User fromEntity(UserEntity entity) {
        return new User(
            entity.getId().intValue(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword()
        );
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este User a una entidad persistente lista para Hibernate.
     */
    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        if (this.id > 0) {
            entity.setId((long) this.id); // int -> Long
        }
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        return entity;
    }

    /**
     * Convierte este User a un DTO de respuesta sin password.
     */
    public UserResponseDto toResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setEmail(this.email);
        dto.setCreatedAt(this.createdAt);
        return dto;
    }

    // ==================== LOGICA DE NEGOCIO ====================

    /**
     * Aplica cambios permitidos en el dominio para una actualización completa.
     */
    public User update(UpdateUserDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        return this;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}