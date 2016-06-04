package sg.ncl.common.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@Configuration
@PropertySource("classpath:/sg/ncl/common/jpa/jpa.properties")
public class JpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") final String url, @Value("${spring.datasource.username}") final String username, @Value("${spring.datasource.password}") final String password) {
        logger.info("Connecting to `{}` as `{}`", url, username);
        return DataSourceBuilder.create().url(url).username(username).password(password).build();
    }

}
