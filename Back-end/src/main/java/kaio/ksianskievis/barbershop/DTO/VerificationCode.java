package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.NotBlank;

public record VerificationCode(@NotBlank String code) {
}
