package sg.ncl.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@Configuration
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}
