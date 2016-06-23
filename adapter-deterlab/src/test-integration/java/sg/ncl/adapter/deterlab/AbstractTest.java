package sg.ncl.adapter.deterlab;

import org.springframework.boot.test.SpringApplicationConfiguration;
import sg.ncl.adapter.deterlab.data.jpa.App;

/**
 * @author Christopher Zhong
 */
@SpringApplicationConfiguration(App.class)
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
