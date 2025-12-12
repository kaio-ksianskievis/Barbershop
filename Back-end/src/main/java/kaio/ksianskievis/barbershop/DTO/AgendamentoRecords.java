package kaio.ksianskievis.barbershop.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRecords(LocalDate data, LocalTime horario, String nomeCliente) {
}
