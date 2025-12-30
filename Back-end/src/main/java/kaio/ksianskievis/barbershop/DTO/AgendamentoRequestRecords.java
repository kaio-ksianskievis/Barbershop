package kaio.ksianskievis.barbershop.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.Model.Agendamento;

import java.time.LocalDateTime;

public record AgendamentoRequestRecords(
        @FutureOrPresent(message = "O agendamento deve ser no futuro!")
        @NotNull(message = "Data não pode ser nula!")
        LocalDateTime dataHora){

    public Agendamento toEntity(){
        Agendamento entity = new Agendamento();
        entity.setDataHora(this.dataHora);

        return  entity;
    }
}
