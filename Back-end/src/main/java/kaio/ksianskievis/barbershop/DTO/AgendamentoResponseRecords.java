package kaio.ksianskievis.barbershop.DTO;

import kaio.ksianskievis.barbershop.Model.Agendamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoResponseRecords(UUID id, String nomeCliente, UUID user_id, LocalDateTime dataHora, LocalDateTime dataCriacao) {
    public AgendamentoResponseRecords(Agendamento agendamento) {
        this(agendamento.getId(), agendamento.getCliente().getNomeCliente(), agendamento.getCliente().getId(), agendamento.getDataHora(), agendamento.getDataCriacao());
    }
}