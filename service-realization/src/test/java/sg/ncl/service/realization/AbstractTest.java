package sg.ncl.service.realization;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Christopher Zhong
 */
//@SpringApplicationConfiguration(App.class)
//public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}

@SpringBootTest(classes = RealizationApplication.class)
@WebAppConfiguration
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}

