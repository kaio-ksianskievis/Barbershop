package kaio.ksianskievis.barbershop.Controller;

import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.DTO.AgendamentoRecords;
import kaio.ksianskievis.barbershop.Model.Agendamentos;
import kaio.ksianskievis.barbershop.Services.Services;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;


@RestController
public class AgendamentoController {

    @Autowired
    private Services services;

    @GetMapping("/agendamentos")
    private ResponseEntity<Object> getAgendamentos(){
        return ResponseEntity.status(HttpStatus.OK).body(services.findAll());
    }

    @GetMapping("/agendamentos/{data}")
    private ResponseEntity<Object> getAgendamentos(@PathVariable  @NotNull LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(services.findByData(data));
    }

    @PostMapping("/agendamentos")
    private ResponseEntity<Object> postAgendamentos(@RequestBody @NotNull AgendamentoRecords data){
        try {
            Agendamentos agendamentos = new Agendamentos();
            BeanUtils.copyProperties(data,agendamentos);
            services.create(agendamentos);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamentos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @DeleteMapping("/agendamentos/{id}")
    private ResponseEntity<Object> deleteAgendamentos(@PathVariable  @NotNull UUID id){
        try {
            services.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento exlcuido com sucesso!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
