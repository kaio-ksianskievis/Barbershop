package kaio.ksianskievis.barbershop.Services;


import kaio.ksianskievis.barbershop.Exception.RegraDeNegocioException;
import kaio.ksianskievis.barbershop.Model.Agendamento;
import kaio.ksianskievis.barbershop.Repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgendamentoServices {

    @Autowired
    private AgendamentoRepository repository;

    public List<Agendamento> getAgendamento(){
        List<Agendamento> busca = repository.findAll();
        return busca;
    }

    public List<Agendamento> getAgendamentoByDataHora(LocalDate data){
        LocalDateTime inicio = data.atTime(0,0);
        LocalDateTime fim = data.atTime(23,59);
        List<Agendamento> busca = repository.findByDataHoraBetweenOrderByDataHoraAsc(inicio,fim);
        return busca;
    }

    public void addAgendamento(Agendamento obj){
        LocalDateTime dataHoraSolicitada = obj.getDataHora();
        LocalDate dataSolicitada = dataHoraSolicitada.toLocalDate();

        LocalDateTime inicioExpediente = dataSolicitada.atTime(8,0);
        LocalDateTime fimExpediente = dataSolicitada.atTime(18,0);

        LocalDateTime fim = dataHoraSolicitada.plusMinutes(29);
        LocalDateTime inicio = dataHoraSolicitada.minusMinutes(29);


        if (dataHoraSolicitada.plusMinutes(30).isAfter(fimExpediente) ||  dataHoraSolicitada.isBefore(inicioExpediente)){
            throw  new RegraDeNegocioException("Barbearia está fechada no  horário solicitado!");
        }
        if(dataSolicitada.getDayOfWeek() == DayOfWeek.SATURDAY || dataSolicitada.getDayOfWeek() == DayOfWeek.SUNDAY){
            throw  new RegraDeNegocioException("Barbearia fecha nos finais de semana!");
        }
        if(repository.existsByDataHoraBetween(inicio,fim)){
            throw  new RegraDeNegocioException("Horário indisponível!");
        }
        repository.save(obj);
    }
    public void deleteAgendamento(UUID id){
        Agendamento agendamento = repository.findById(id).orElseThrow(()-> new RegraDeNegocioException("Agendamento não encontrado!") );
        repository.delete(agendamento);
    }

    public List<LocalTime> getHorariosLivres(LocalDate data) {

        LocalDateTime inicio = data.atTime(8, 0);
        LocalDateTime fim = data.atTime(18, 0);
        List<Agendamento> agendamentos = repository.findByDataHoraBetweenOrderByDataHoraAsc(inicio, fim);

        Set<LocalTime> horariosOcupados = agendamentos.stream()
                .map(a -> a.getDataHora().toLocalTime())
                .collect(Collectors.toSet());

        List<LocalTime> horariosLivres = new ArrayList<>();
        LocalTime horarioAtual = LocalTime.of(8, 0);
        LocalTime fechamento = LocalTime.of(18, 0);

        while (horarioAtual.isBefore(fechamento)) {
            if (!horariosOcupados.contains(horarioAtual)) {
                horariosLivres.add(horarioAtual);
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }

        return horariosLivres;
    }

}
