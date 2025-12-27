package kaio.ksianskievis.barbershop.DTO;

import java.time.LocalDateTime;

public record MensagemErro(int status, String erro, LocalDateTime dataHora) {
}
