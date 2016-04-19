package sg.ncl.testbed_interface;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class JpaConfig {
}
