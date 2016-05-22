package sg.ncl.service.experiment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Christopher Zhong
 */
@Configuration("sg.ncl.service.experiment.JpaConfig")
@Import({sg.ncl.common.jpa.JpaConfig.class})
@EnableTransactionManagement
public class JpaConfig {}
