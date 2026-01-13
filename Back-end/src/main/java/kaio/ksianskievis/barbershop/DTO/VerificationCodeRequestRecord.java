package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.NotBlank;

public record VerificationCodeRequestRecord(@NotBlank String code) {
}
