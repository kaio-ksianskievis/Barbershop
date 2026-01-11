package kaio.ksianskievis.barbershop.DTO;

import kaio.ksianskievis.barbershop.Model.User;
import java.util.UUID;

public record UserResponseRecord(UUID id, String email, UserRole role, String nomeCliente) {

    public UserResponseRecord(User user) {
        this(user.getId(), user.getEmail(), user.getRole(),user.getName());
    }
}