package kaio.ksianskievis.barbershop.Services;

import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.Model.Agendamentos;
import kaio.ksianskievis.barbershop.Repository.AgendamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class Services {

    @Autowired
    private AgendamentosRepository repository;


    public ResponseEntity<Object>  findAll(){
        return  ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    public ResponseEntity<Object> findByData( LocalDate data){
        return  ResponseEntity.status(HttpStatus.OK).body(repository.findByData(data));
    }

   public  ResponseEntity<Object> create(  Agendamentos novoAgendamento){
        if(novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SUNDAY || novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SATURDAY){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A barbearia não funciona aos finais de semana!");
        }
       List<Agendamentos> agendamentosDia = repository.findByData(novoAgendamento.getData());
        for(Agendamentos agendamentos : agendamentosDia){
            long diferencaEmMinutos = ChronoUnit.MINUTES.between(
                    agendamentos.getHorario(),
                   novoAgendamento.getHorario()
            );

            if (Math.abs(diferencaEmMinutos) < 30) {
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já está reservado esse horário para outro cliente");
            }
        }
        repository.save(novoAgendamento);
        return  ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
   }

   public  ResponseEntity<Object> deleteById( UUID id){
        repository.deleteById(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Agendamento excluido com sucesso!");
   }

}
