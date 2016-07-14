package sg.ncl.common.jpa;

import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import sg.ncl.common.test.AbstractTest;

import javax.inject.Inject;
import javax.sql.DataSource;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
@SpringApplicationConfiguration(TestApp.class)
public class JpaAutoConfigurationTest extends AbstractTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    public void testDataSource() throws Exception {
        final DataSource bean = applicationContext.getBean(DataSource.class);

        assertThat(bean, is(not(nullValue(DataSource.class))));
    }

}
