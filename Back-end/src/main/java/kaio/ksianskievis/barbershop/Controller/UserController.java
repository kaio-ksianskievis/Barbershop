package kaio.ksianskievis.barbershop.Controller;


import jakarta.validation.Valid;
import kaio.ksianskievis.barbershop.DTO.LoginRecord;
import kaio.ksianskievis.barbershop.DTO.LoginResponse;
import kaio.ksianskievis.barbershop.DTO.RegisterRecord;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Services.TokenService;
import kaio.ksianskievis.barbershop.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<Object> getUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios/{email}")
    public ResponseEntity<Object> getUsuariosByEmail(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(service.getByemail(email));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/usuarios/{id}")
    public  ResponseEntity<Object> deleteUser( @PathVariable UUID id){
        service.deleteUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Usuário Deletado");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addUser(@RequestBody @Valid RegisterRecord body){
        User usuario = body.toEntity();
        service.addUser(usuario);

        return  ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado!");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRecord data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }

}
