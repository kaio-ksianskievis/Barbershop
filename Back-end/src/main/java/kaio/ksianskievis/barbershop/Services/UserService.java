package kaio.ksianskievis.barbershop.Services;

import kaio.ksianskievis.barbershop.DTO.UserResponseRecord;
import kaio.ksianskievis.barbershop.DTO.VerificationCodeResponseRecord;
import kaio.ksianskievis.barbershop.Exception.RegraDeNegocioException;
import kaio.ksianskievis.barbershop.Model.User;
import kaio.ksianskievis.barbershop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    public List<UserResponseRecord> getUser(){
        List<UserResponseRecord> busca = repository.findAll().stream().map(UserResponseRecord::new).toList();
        return  busca;
    }

    public UserResponseRecord getByemail(String email){
        User usuario = repository.findByEmail(email).orElseThrow(()-> new RegraDeNegocioException("Email não encontrado!"));
        UserResponseRecord novoUsuario =  new UserResponseRecord(usuario);
        return novoUsuario;
    }

    public UserResponseRecord addUser(User user){
        if(repository.existsByEmail(user.getEmail())){
            throw new RegraDeNegocioException("Email já cadastrado!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(1000000);
        String codigo = String.format("%06d",code);
        System.out.println("CODIGO: "+codigo);
        user.setCode(codigo);
        repository.save(user);
        UserResponseRecord response = new UserResponseRecord(user);


        Context context = new Context();
        context.setVariable("nome",user.getName());
        context.setVariable("codigoVerificacao",codigo);
        mailService.sendEmail(user.getEmail(),"Seu código de verificação: "+ codigo,context,"email-verificacao");

        return  response;
    }

    public void deleteUser(UUID id){
        User busca = repository.findById(id).orElseThrow(()-> new RegraDeNegocioException("Id não encontrado!"));
        repository.delete(busca);
    }

    public VerificationCodeResponseRecord verifyCodeUser(String codigo){
        User usuario = repository.findByCode(codigo).orElseThrow(()-> new RegraDeNegocioException("Código inválido"));
        usuario.setStatus(true);
        usuario.setCode(null);
        repository.save(usuario);
        return new VerificationCodeResponseRecord("email verificado!");
    }
    @Override
    public UserDetails loadUserByUsername(String email)  {
        return  repository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado!"));
    }
}
