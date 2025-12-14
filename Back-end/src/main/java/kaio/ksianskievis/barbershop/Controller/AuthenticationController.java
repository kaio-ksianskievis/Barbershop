package kaio.ksianskievis.barbershop.Controller;

import jakarta.validation.Valid;
import kaio.ksianskievis.barbershop.DTO.LoginDTO;
import kaio.ksianskievis.barbershop.DTO.LoginResponseDTO;
import kaio.ksianskievis.barbershop.DTO.SignUpDTO;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Repository.UserRepository;
import kaio.ksianskievis.barbershop.Services.TokenService;
import kaio.ksianskievis.barbershop.Services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public  ResponseEntity<Object>  signUp(@RequestBody SignUpDTO data){
        try {
            User usuario = new User();
            BeanUtils.copyProperties(data,usuario);
            service.createUser(usuario);
            return  ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}