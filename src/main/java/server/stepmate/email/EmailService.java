package server.stepmate.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import server.stepmate.config.response.exception.CustomException;
import server.stepmate.config.response.exception.CustomExceptionStatus;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    public void sendEmail(String toEmail,
                          String title,
                          String text)  {
        MimeMessage message = null;
        try {
            message = createEmailForm(toEmail, title, text);
        } catch (MessagingException e) {
            throw new CustomException(CustomExceptionStatus.USER_EMAIL_ERROR);
        }

        javaMailSender.send(message);
    }

    private MimeMessage createEmailForm(String toEmail, String title, String authCode) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(title);

        Context context = new Context();
        context.setVariable("authCode",authCode);

        String html = templateEngine.process("index", context);
        helper.setText(html,true);

        helper.addInline("image1", new ClassPathResource("static/images/image-1.png"));
        helper.addInline("image2", new ClassPathResource("static/images/image-2.png"));
        helper.addInline("image3", new ClassPathResource("static/images/image-3.png"));
        helper.addInline("image4", new ClassPathResource("static/images/image-4.png"));
        helper.addInline("image5", new ClassPathResource("static/images/image-5.png"));

        return mimeMessage;
    }


}
