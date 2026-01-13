package kaio.ksianskievis.barbershop.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kaio.ksianskievis.barbershop.DTO.AgendamentoRequestRecords;
import kaio.ksianskievis.barbershop.DTO.AgendamentoResponseRecords;
import kaio.ksianskievis.barbershop.Model.Agendamento;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Services.AgendamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@RestController
public class AgendamentoController {

    @Autowired
    private AgendamentoServices services;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agendamentos")
    public ResponseEntity<List<AgendamentoResponseRecords>> getAgendamentos(){
        return ResponseEntity.status(HttpStatus.OK).body(services.getAgendamento());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agendamentos/{data}")
    public ResponseEntity<List<AgendamentoResponseRecords>> getAgendamentos(@PathVariable @NotNull LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(services.getAgendamentoByDataHora(data));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/agendamentos/me")
    public ResponseEntity<List<AgendamentoResponseRecords> > getAgendamentos(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(services.getAgendamentoByUser(user));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/horarios/{data}")
    public  ResponseEntity<List<LocalTime>> getHorarios(@PathVariable  LocalDate data){
        return  ResponseEntity.status(HttpStatus.OK).body(services.getHorariosLivres(data));
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/agendamentos")
    public ResponseEntity<AgendamentoResponseRecords> postAgendamentos(@RequestBody @Valid AgendamentoRequestRecords body, @AuthenticationPrincipal User user){
            Agendamento agendamento = body.toEntity();
            agendamento.setCliente(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(services.addAgendamento(agendamento));
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/agendamentos/{id}")
    public ResponseEntity<Object> deleteAgendamentos(@PathVariable  UUID id){
            services.deleteAgendamento(id);
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento exlcuido com sucesso!");

    }

}
