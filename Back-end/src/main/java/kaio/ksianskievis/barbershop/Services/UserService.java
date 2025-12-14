package kaio.ksianskievis.barbershop.Services;

import kaio.ksianskievis.barbershop.DTO.UserRoles;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User usuario){

        User busca = repository.findByEmail(usuario.getEmail());
        if(busca != null){
            throw new IllegalArgumentException("Já possui um usuário com esse email");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        repository.save(usuario);
        return usuario;

    }

}
