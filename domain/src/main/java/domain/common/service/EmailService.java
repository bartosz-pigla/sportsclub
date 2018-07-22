package domain.common.service;

import static org.slf4j.LoggerFactory.getLogger;

import javax.mail.internet.MimeMessage;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class EmailService {

    private static final Logger logger = getLogger(EmailService.class);

    private JavaMailSender javaMailSender;

    public void send(String to, String subject, String text) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mail);
            logger.info("Send email '{}' to: {}", subject, to);
        } catch (Exception e) {
            logger.error("Problem with sending email to: {}, error message: {}", to, e.getMessage());
        }
    }
}
