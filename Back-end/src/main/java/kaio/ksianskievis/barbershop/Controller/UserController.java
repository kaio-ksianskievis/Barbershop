package kaio.ksianskievis.barbershop.Controller;


import jakarta.validation.Valid;
import kaio.ksianskievis.barbershop.DTO.*;
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

import java.util.List;
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
    public ResponseEntity<List<UserResponseRecord>> getUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios/{email}")
    public ResponseEntity<UserResponseRecord> getUsuariosByEmail(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(service.getByemail(email));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/usuarios/{id}")
    public  ResponseEntity<Object> deleteUser( @PathVariable UUID id){
        service.deleteUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Usuário Deletado");
    }
    @PostMapping("/verify")
    public ResponseEntity<VerificationCodeResponseRecord> verifyUser(@RequestBody @Valid VerificationCodeRequestRecord obj){
        return  ResponseEntity.status(HttpStatus.OK).body(service.verifyCodeUser(obj.code()));
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseRecord> addUser(@RequestBody @Valid RegisterRequestRecord body){
        User usuario = body.toEntity();
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.addUser(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseRecord> login(@RequestBody @Valid LoginRequestRecord data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
