package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.Model.Agendamento;

import java.time.LocalDateTime;

public record AgendamentoRecords(
        @FutureOrPresent(message = "O agendamento deve ser no futuro!")
        @NotNull(message = "Data não pode ser nula!")
        LocalDateTime dataHora,
        @NotBlank(message = "Nome não pode ser nulo!")
        String nomeCliente) {

    public Agendamento toEntity(){
        Agendamento entity = new Agendamento();
        entity.setNomeCliente(this.nomeCliente);
        entity.setDataHora(this.dataHora);

        return  entity;
    }
}
