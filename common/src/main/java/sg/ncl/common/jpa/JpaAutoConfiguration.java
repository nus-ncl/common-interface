package sg.ncl.common.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

/**
 * @author Christopher Zhong
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class JpaAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JpaAutoConfiguration.class);

    private final DataSourceProperties properties;

    @Inject
    JpaAutoConfiguration(@NotNull final DataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource dataSource() {
        logger.info("Connecting to '{}' as '{}'", properties.getUrl(), properties.getUsername());
        return DataSourceBuilder.create().url(properties.getUrl()).username(properties.getUsername()).password(properties.getPassword()).build();
    }

}
