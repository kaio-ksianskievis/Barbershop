package kaio.ksianskievis.barbershop.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String code,String username){
        try {
            String message = "Olá "+username+" seu código de verificação é: "+code+"!";
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("SEU EMAIL!");
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Código de verificação");
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
