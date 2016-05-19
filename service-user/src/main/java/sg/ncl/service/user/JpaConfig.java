package sg.ncl.service.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.user.JpaConfig")
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}
