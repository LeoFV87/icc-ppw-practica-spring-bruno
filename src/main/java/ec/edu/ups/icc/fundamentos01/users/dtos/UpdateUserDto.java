package ec.edu.ups.icc.fundamentos01.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {
    @NotBlank
    @Size(min = 3, max = 150)
    public String name;

    @NotBlank
    @Email
    @Size(max = 150)
    public String email;

    @NotBlank
    @Size(min = 8)
    public String password;

   // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}