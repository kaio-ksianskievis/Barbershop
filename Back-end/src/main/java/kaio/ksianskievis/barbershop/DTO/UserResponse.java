package kaio.ksianskievis.barbershop.DTO;

import kaio.ksianskievis.barbershop.Model.User;
import java.util.UUID;

public record UserResponse(UUID id, String email, UserRole role) {

    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getRole());
    }
}