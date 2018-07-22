package boot;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
class LocaleConfiguration {

    @Bean
    public MessageSourceAccessor getMessageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.forLanguageTag("pl"));
    }
}
