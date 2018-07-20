package domain.common.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class EmailService {

    private JavaMailSender emailSender;

    public void sendActivationLink() {

    }
}
