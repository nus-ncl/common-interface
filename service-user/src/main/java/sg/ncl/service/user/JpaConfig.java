package sg.ncl.service.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.user.JpaConfig")
@Import({sg.ncl.common.jpa.JpaConfig.class})
@EnableTransactionManagement
public class JpaConfig {}
