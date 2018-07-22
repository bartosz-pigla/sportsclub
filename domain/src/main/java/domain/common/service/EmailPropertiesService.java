package domain.common.service;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public final class EmailPropertiesService {

    @Value("${mailing.activationLink}")
    @Getter
    private String activationLink;
    @Getter
    private String subject;
    @Setter(onMethod_ = { @Autowired })
    private MessageSourceAccessor accessor;

    @PostConstruct
    public void initialize() {
        subject = accessor.getMessage("mailing.subject");
    }
}
