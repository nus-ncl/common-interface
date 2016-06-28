package sg.ncl.common.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author Christopher Zhong
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(DataSourceProperties.class)
@PropertySource("classpath:/sg/ncl/common/jpa/jpa.properties")
public class JpaAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JpaAutoConfiguration.class);

    @Inject
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource() {
        logger.info("Connecting to `{}` as `{}`", dataSourceProperties.getUrl(), dataSourceProperties.getUsername());
        return DataSourceBuilder.create().url(dataSourceProperties.getUrl()).username(dataSourceProperties.getUsername()).password(dataSourceProperties.getPassword()).build();
    }

}
