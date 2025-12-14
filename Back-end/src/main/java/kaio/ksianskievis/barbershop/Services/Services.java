package kaio.ksianskievis.barbershop.Services;

import kaio.ksianskievis.barbershop.Model.Agendamentos;
import kaio.ksianskievis.barbershop.Repository.AgendamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class Services {

    @Autowired
    private AgendamentosRepository repository;


    public List<Agendamentos> findAll(){
        return repository.findAll();
    }

    public List<Agendamentos> findByData( LocalDate data){
        return  repository.findByData(data);
    }

   public  Agendamentos create( Agendamentos novoAgendamento){

       LocalTime oitoDaManha = LocalTime.of(8,0);
       LocalTime seisDaTarde = LocalTime.of(18,0);

        if(novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SUNDAY || novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SATURDAY){
            throw new IllegalArgumentException("A barbearia não funciona aos finais de semana!");
        }

        if(novoAgendamento.getHorario().isBefore(oitoDaManha) || novoAgendamento.getHorario().plusMinutes(30).isAfter(seisDaTarde)){
            throw new IllegalArgumentException("A barbearia está fechada nesse horário!");
        }

        List<Agendamentos> agendamentosDia = repository.findByData(novoAgendamento.getData());

        for(Agendamentos agendamentos : agendamentosDia){
            long diferencaEmMinutos = ChronoUnit.MINUTES.between(
                    agendamentos.getHorario(),
                   novoAgendamento.getHorario()
            );

            if (Math.abs(diferencaEmMinutos) < 30) {
                throw new IllegalArgumentException("Já está reservado esse horário para outro cliente");
            }
        }

        repository.save(novoAgendamento);
        return  novoAgendamento;
   }

   public  void deleteById( UUID id){
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("ID não encontrado");
        }
        repository.deleteById(id);
   }

   public List<LocalTime> horariosLivres(LocalDate data){

       LocalTime inicio = LocalTime.of(8, 0);
       LocalTime fim = LocalTime.of(18, 0);

       List<LocalTime> todosHorarios = new ArrayList<>();
       LocalTime atual = inicio;
       while (atual.isBefore(fim)) {
           todosHorarios.add(atual);
           atual = atual.plusMinutes(30);
       }

       List<Agendamentos> agendamentosDoDia = repository.findByData(data);

       List<LocalTime> horariosOcupados = agendamentosDoDia.stream()
               .map(Agendamentos::getHorario)
               .toList();

       todosHorarios.removeAll(horariosOcupados);

       return todosHorarios;
   }

}
