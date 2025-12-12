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
import java.util.UUID;

@Service
public class Services {

    @Autowired
    private AgendamentosRepository repository;


    public ResponseEntity<Object>  findAll(){
        return  ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    public ResponseEntity<Object> findByData(@NotNull LocalDate data){
        return  ResponseEntity.status(HttpStatus.OK).body(repository.findByData(data));
    }

   public  ResponseEntity<Object> create(@NotNull  Agendamentos agendamento){
        if(agendamento.getData().getDayOfWeek() == DayOfWeek.SUNDAY || agendamento.getData().getDayOfWeek() == DayOfWeek.SATURDAY){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A barbearia não funciona aos finais de semana!");
        }
        repository.save(agendamento);
        return  ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
   }

   public  ResponseEntity<Object> deleteById( @NotNull UUID id){
        repository.deleteById(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Agendamento excluido com sucesso!");
   }

}
