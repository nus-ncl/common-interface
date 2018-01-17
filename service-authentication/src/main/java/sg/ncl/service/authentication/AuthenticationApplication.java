package sg.ncl.service.authentication;

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
import sg.ncl.common.authentication.AuthenticationAutoConfiguration;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.jpa.UseJpa;
import sg.ncl.common.jwt.JwtAutoConfiguration;
import sg.ncl.service.mail.MailApplication;

import java.io.IOException;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
@UseJpa
@Import({
        AuthenticationAutoConfiguration.class,
        ExceptionAutoConfiguration.class,
        JwtAutoConfiguration.class,
        DeterLabAutoConfiguration.class,
        MailApplication.class,
        DomainProperties.class
})
public class AuthenticationApplication {

    private static final String RESET_EMAIL_TEMPLATE_NAME = "passwordResetEmailTemplate.ftl";

    public static void main(final String[] args) {
        try (final ConfigurableApplicationContext context = SpringApplication.run(AuthenticationApplication.class, args)) {
            // nothing to do
        }
    }

    @Bean
    Template passwordResetEmailTemplate(final Configuration configuration) throws IOException {
        return configuration.getTemplate(RESET_EMAIL_TEMPLATE_NAME);
    }

}
