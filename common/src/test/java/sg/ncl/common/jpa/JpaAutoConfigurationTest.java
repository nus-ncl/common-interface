package sg.ncl.common.jpa;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class JpaAutoConfigurationTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private DataSourceProperties dataSourceProperties;
    @Mock
    private JpaProperties jpaProperties;

    private JpaAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new JpaAutoConfiguration(dataSourceProperties, jpaProperties);
    }

//    @Test
//    public void testDataSource() throws Exception {
//        final DataSource dataSource = configuration.dataSource();
//
//        assertThat(dataSource, is(not(nullValue(DataSource.class))));
//    }

}
