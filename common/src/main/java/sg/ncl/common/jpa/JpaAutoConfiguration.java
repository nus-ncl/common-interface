package sg.ncl.common.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

/**
 * @author Christopher Zhong
 */
@Configuration
@ConditionalOnClass({DataSource.class, JpaProperties.class})
@EnableConfigurationProperties({DataSourceProperties.class, JpaProperties.class})
@Slf4j
public class JpaAutoConfiguration {

    private final DataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;

    @Inject
    JpaAutoConfiguration(@NotNull final DataSourceProperties dataSourceProperties, @NotNull final JpaProperties jpaProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.jpaProperties = jpaProperties;
    }

}
