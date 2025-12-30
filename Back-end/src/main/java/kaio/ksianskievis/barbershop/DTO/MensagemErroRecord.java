package kaio.ksianskievis.barbershop.DTO;

import java.time.LocalDateTime;

public record MensagemErroRecord(int status, String erro, LocalDateTime dataHora) {
}
