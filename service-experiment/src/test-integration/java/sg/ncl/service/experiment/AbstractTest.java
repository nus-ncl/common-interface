package sg.ncl.service.experiment;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Christopher Zhong
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = TestConfig.class)
//@WebAppConfiguration
//public abstract class AbstractTest extends AbstractJUnit4SpringContextTests {
//}

@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
