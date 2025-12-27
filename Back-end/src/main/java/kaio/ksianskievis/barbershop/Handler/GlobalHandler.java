package kaio.ksianskievis.barbershop.Handler;


import kaio.ksianskievis.barbershop.DTO.MensagemErro;
import kaio.ksianskievis.barbershop.Exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Object> regraDeNegocio(RegraDeNegocioException e){
        MensagemErro mensagemErro = new MensagemErro(HttpStatus.BAD_REQUEST.value(),e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> notValid(MethodArgumentNotValidException e){
        int status = HttpStatus.BAD_REQUEST.value();

        List<FieldError> listaErros = e.getBindingResult().getFieldErrors();

        List<MensagemErro> mensagensDeErro = listaErros.stream().map(
                erro -> new MensagemErro(status,erro.getDefaultMessage(),LocalDateTime.now())).toList();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensDeErro);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFound(UsernameNotFoundException e){
        MensagemErro mensagemErro = new MensagemErro(HttpStatus.BAD_REQUEST.value(),e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }
}
