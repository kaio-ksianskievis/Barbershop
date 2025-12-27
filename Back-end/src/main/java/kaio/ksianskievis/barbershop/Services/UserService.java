package kaio.ksianskievis.barbershop.Services;

import kaio.ksianskievis.barbershop.DTO.UserResponse;
import kaio.ksianskievis.barbershop.Exception.RegraDeNegocioException;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponse> getUser(){
        List<UserResponse> busca = repository.findAll().stream().map(UserResponse::new).toList();
        return  busca;
    }

    public UserResponse getByemail(String email){
        User usuario = repository.findByEmail(email).orElseThrow(()-> new RegraDeNegocioException("Email não encontrado!"));
        UserResponse novoUsuario =  new UserResponse(usuario);
        return novoUsuario;
    }

    public void addUser(User user){
        if(repository.existsByEmail(user.getEmail())){
            throw new RegraDeNegocioException("Email já cadastrado!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public void deleteUser(UUID id){
        User busca = repository.findById(id).orElseThrow(()-> new RegraDeNegocioException("Id não encontrado!"));
        repository.delete(busca);
    }

    @Override
    public UserDetails loadUserByUsername(String email)  {
        return  repository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado!"));
    }
}
