package sg.ncl.service.experiment;

import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by Desmond.
 */
public class ConnectionPropertiesTest extends AbstractTest {

    @Inject
    private ConnectionProperties connectionProperties;

    @Test
    public void testGetBossUrlReturnsNotNull() throws Exception {
        String bossUrl = connectionProperties.getBossurl();

        Assert.assertNotNull(bossUrl);
        Assert.assertTrue(bossUrl.length() > 0);
    }

    @Test
    public void testGetUserUrlReturnsNotNull() throws Exception {
        String userUrl = connectionProperties.getUserurl();

        Assert.assertNotNull(userUrl);
        Assert.assertTrue(userUrl.length() > 0);
    }

    @Test
    public void testGetPortReturnsNotNull() throws Exception {
        String port = connectionProperties.getPort();

        Assert.assertNotNull(port);
        Assert.assertTrue(port.length() > 0);
    }
}
