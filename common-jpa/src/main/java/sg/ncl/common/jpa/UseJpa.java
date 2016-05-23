package sg.ncl.common.jpa;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
public @interface UseJpa {}
