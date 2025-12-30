package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kaio.ksianskievis.barbershop.Model.User;

public record RegisterRequestRecord(
        @Email(message = "Email inválido!")
        @NotBlank(message = "Email não pode ser nulo!")
        String email,
        @NotBlank(message = "Senha não pode ser nula!")
        @Size(min = 8,message = "Senha deve ter no minímo 8 caracteres!")
        String password,
        @NotNull(message = "Role não pode ser nula!")
        UserRole role,
        @NotBlank(message = "Nome não pode ser nulo!")
        String nomeCliente) {

    public User toEntity(){
        User usuarios = new User();
        usuarios.setPassword(this.password);
        usuarios.setEmail(this.email);
        usuarios.setRole(this.role);
        usuarios.setNomeCliente(this.nomeCliente);
        return usuarios;
    }
}
