package kaio.ksianskievis.barbershop.Handler;


import kaio.ksianskievis.barbershop.DTO.MensagemErroRecord;
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
        MensagemErroRecord mensagemErro = new MensagemErroRecord(HttpStatus.BAD_REQUEST.value(),e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> notValid(MethodArgumentNotValidException e){
        int status = HttpStatus.BAD_REQUEST.value();

        List<FieldError> listaErros = e.getBindingResult().getFieldErrors();

        List<MensagemErroRecord> mensagensDeErro = listaErros.stream().map(
                erro -> new MensagemErroRecord(status,erro.getDefaultMessage(),LocalDateTime.now())).toList();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensDeErro);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFound(UsernameNotFoundException e){
        MensagemErroRecord mensagemErro = new MensagemErroRecord(HttpStatus.BAD_REQUEST.value(),e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }
}
