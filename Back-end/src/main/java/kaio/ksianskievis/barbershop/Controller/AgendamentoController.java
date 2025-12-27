package kaio.ksianskievis.barbershop.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.DTO.AgendamentoRecords;
import kaio.ksianskievis.barbershop.Model.Agendamento;
import kaio.ksianskievis.barbershop.Services.AgendamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;


@RestController
public class AgendamentoController {

    @Autowired
    private AgendamentoServices services;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agendamentos")
    public ResponseEntity<Object> getAgendamentos(){
        return ResponseEntity.status(HttpStatus.OK).body(services.getAgendamento());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agendamentos/{data}")
    public ResponseEntity<Object> getAgendamentos(@PathVariable @NotNull LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(services.getAgendamentoByDataHora(data));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/horarios/{data}")
    public  ResponseEntity<Object> getHorarios(@PathVariable  LocalDate data){
        return  ResponseEntity.status(HttpStatus.OK).body(services.getHorariosLivres(data));
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/agendamentos")
    public ResponseEntity<Object> postAgendamentos(@RequestBody @Valid AgendamentoRecords body){
            Agendamento agendamento = body.toEntity();
            services.addAgendamento(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body("Agendamento criado!");
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/agendamentos/{id}")
    public ResponseEntity<Object> deleteAgendamentos(@PathVariable  UUID id){
            services.deleteAgendamento(id);
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento exlcuido com sucesso!");

    }

}
