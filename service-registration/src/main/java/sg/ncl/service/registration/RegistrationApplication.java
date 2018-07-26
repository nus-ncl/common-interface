package sg.ncl.service.registration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sg.ncl.adapter.deterlab.DeterLabAutoConfiguration;
import sg.ncl.adapter.openstack.OpenStackAutoConfiguration;
import sg.ncl.common.DomainProperties;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.service.authentication.AuthenticationApplication;
import sg.ncl.service.mail.MailApplication;
import sg.ncl.service.team.TeamApplication;
import sg.ncl.service.user.UserApplication;

import java.io.IOException;

/**
 * @author Christopher Zhong, Tran Ly Vu
 */
@SpringBootApplication
@UseJpa
@Import({
        AuthenticationApplication.class,
        TeamApplication.class,
        UserApplication.class,
        DeterLabAutoConfiguration.class,
        OpenStackAutoConfiguration.class,
        MailApplication.class,
        DomainProperties.class
})
public class RegistrationApplication {

    private static final String VERIFICATION_EMAIL_TEMPLATE_NAME = "verificationEmailTemplate.ftl";
    private static final String APPLY_CREATE_TEAM_REQUEST_TEMPLATE_NAME = "applyCreateTeamRequestTemplate.ftl";
    private static final String REPLY_CREATE_TEAM_REQUEST_TEMPLATE_NAME = "replyCreateTeamRequestTemplate.ftl";
    private static final String APPLY_JOIN_TEAM_REQUEST_TEMPLATE_NAME = "applyJoinTeamRequestTemplate.ftl";
    private static final String REPLY_JOIN_TEAM_REQUEST_TEMPLATE_NAME = "replyJoinTeamRequestTemplate.ftl";

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
    Template applyCreateTeamRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(APPLY_CREATE_TEAM_REQUEST_TEMPLATE_NAME);
    }

    @Bean
    Template replyCreateTeamRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(REPLY_CREATE_TEAM_REQUEST_TEMPLATE_NAME);
    }

    @Bean
    Template applyJoinTeamRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(APPLY_JOIN_TEAM_REQUEST_TEMPLATE_NAME);
    }

    @Bean
    Template replyJoinTeamRequestTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(REPLY_JOIN_TEAM_REQUEST_TEMPLATE_NAME);
    }

}
