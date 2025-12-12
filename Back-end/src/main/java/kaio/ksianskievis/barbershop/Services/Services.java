package kaio.ksianskievis.barbershop.Services;

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


    public List<Agendamentos> findAll(){
        return repository.findAll();
    }

    public List<Agendamentos> findByData( LocalDate data){
        return  repository.findByData(data);
    }

   public  Agendamentos create(  Agendamentos novoAgendamento){
        if(novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SUNDAY || novoAgendamento.getData().getDayOfWeek() == DayOfWeek.SATURDAY){
            throw new IllegalArgumentException("A barbearia não funciona aos finais de semana!");
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

}
