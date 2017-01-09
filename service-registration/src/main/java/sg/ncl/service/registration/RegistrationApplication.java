package sg.ncl.service.registration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.mail.MailApplication;
import sg.ncl.service.team.TeamApplication;
import sg.ncl.service.user.UserApplication;

import java.io.IOException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        AuthenticationApplication.class,
        TeamApplication.class,
        UserApplication.class,
        DeterLabAutoConfiguration.class,
        MailApplication.class,
        DomainProperties.class
})
public class RegistrationApplication {

    private static final String VERIFICATION_EMAIL_TEMPLATE_NAME = "verificationEmailTemplate.ftl";
    private static final String APPLY_TEAM_REQUEST_TEMPLATE_NAME = "applyTeamRequestTemplate.ftl";

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(RegistrationApplication.class, args)) {
            // nothing to do
        }
    }

    @Bean
    Template emailValidationTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(VERIFICATION_EMAIL_TEMPLATE_NAME);
    }

    @Bean
    Template applyTeamRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(APPLY_TEAM_REQUEST_TEMPLATE_NAME);
    }

}
