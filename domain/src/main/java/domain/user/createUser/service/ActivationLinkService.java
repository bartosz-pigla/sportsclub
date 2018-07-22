package domain.user.createUser.service;

import java.util.UUID;

import domain.common.service.EmailPropertiesService;
import domain.common.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import query.model.embeddable.Email;

@Service
@AllArgsConstructor
public final class ActivationLinkService {

    private static final String templateName = "email-template";

    private TemplateEngine templateEngine;
    private EmailService emailService;
    private EmailPropertiesService propertiesService;

    public void send(Email recipient, UUID activationKey) {
        Context context = new Context();
        context.setVariable("title", propertiesService.getSubject());
        context.setVariable("activationLink", propertiesService.getActivationLink() + activationKey.toString());
        emailService.send(recipient.getEmail(), propertiesService.getSubject(), templateEngine.process(templateName, context));
    }
}
