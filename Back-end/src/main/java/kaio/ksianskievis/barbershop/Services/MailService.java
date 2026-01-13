package kaio.ksianskievis.barbershop.Services;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendEmail(String destinatario, String subject, Context context,String nomeArquivo){

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = springTemplateEngine.process(nomeArquivo, context);
            
            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
