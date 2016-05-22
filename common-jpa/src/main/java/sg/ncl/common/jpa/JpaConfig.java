package sg.ncl.common.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.common.jpa.JpaConfig")
@EnableJpaRepositories
@EnableJpaAuditing
public class JpaConfig {}
