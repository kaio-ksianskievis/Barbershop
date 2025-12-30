package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestRecord(
        @Email(message = "Email inválido!")
        @NotBlank(message = "Email não pode ser nulo!")
        String email,
        @NotBlank(message = "Senha não pode ser nula!")
        String password
) {}
