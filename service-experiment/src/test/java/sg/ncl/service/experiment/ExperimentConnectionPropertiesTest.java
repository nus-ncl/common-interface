package sg.ncl.service.experiment;

import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by Desmond.
 */
public class ExperimentConnectionPropertiesTest extends AbstractTest {

    @Inject
    private ExperimentConnectionProperties experimentConnectionProperties;

    @Test
    public void testGetBossUrlReturnsNotNull() throws Exception {
        String bossUrl = experimentConnectionProperties.getBossurl();

        Assert.assertNotNull(bossUrl);
        Assert.assertTrue(bossUrl.length() > 0);
    }

    @Test
    public void testGetUserUrlReturnsNotNull() throws Exception {
        String userUrl = experimentConnectionProperties.getUserurl();

        Assert.assertNotNull(userUrl);
        Assert.assertTrue(userUrl.length() > 0);
    }

    @Test
    public void testGetPortReturnsNotNull() throws Exception {
        String port = experimentConnectionProperties.getPort();

        Assert.assertNotNull(port);
        Assert.assertTrue(port.length() > 0);
    }
}
