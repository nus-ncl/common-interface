
package sg.ncl.adapter.deterlab;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.adapter.deterlab.ConnectionPropertiesTest.TestConfig;
import javax.inject.Inject;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @Author Tran Ly Vu on 10/3/2016.
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes =TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "ncl.deterlab.adapter.ip=127.0.0.1",
        "ncl.deterlab.adapter.port=22",
        "ncl.deterlab.adapter.bossUrl=boss.ncl.sg",
        "ncl.deterlab.adapter.userUrl=users.ncl.sg",
})

public class ConnectionPropertiesTest {


    @Configuration
    @EnableConfigurationProperties(ConnectionProperties.class)
    static class TestConfig {
    }

    @Inject
    private ConnectionProperties connectionProperties;


    @Test
    public void testIp() throws Exception {
        assertThat(connectionProperties.getIp()).isEqualTo("127.0.0.1");
    }

    @Test
    public void testPort() throws Exception {
        assertThat(connectionProperties.getPort()).isEqualTo("22");
    }

    @Test
    public void othersTest(){

        String actual1= connectionProperties.getJoinProjectNewUsers();
        String actual2= connectionProperties.getJoinProject();
        String actual3= connectionProperties.getApplyProjectNewUsers();
        String actual4= connectionProperties.getCreateExperiment();
        String actual5= connectionProperties.startExperiment();
        String actual6= connectionProperties.stopExperiment() ;
        String actual7= connectionProperties.deleteExperiment();
        String actual8= connectionProperties.getUpdateCredentials();
        String actual9= connectionProperties.getApproveJoinRequest();
        String actual10= connectionProperties.getRejectJoinRequest();
        String actual11= connectionProperties.getApplyProject();
        String actual12= connectionProperties.getApproveProject();
        String actual13= connectionProperties.getRejectProject();
        String actual14= connectionProperties.getExpStatus();


        String expected1="http://127.0.0.1:22/joinProjectNewUsers";
        String expected2="http://127.0.0.1:22/joinProject";
        String expected3="http://127.0.0.1:22/applyProjectNewUsers";
        String expected4="http://127.0.0.1:22/createExperiment";
        String expected5="http://127.0.0.1:22/startExperiment";
        String expected6="http://127.0.0.1:22/stopExperiment";
        String expected7="http://127.0.0.1:22/deleteExperiment";
        String expected8="http://127.0.0.1:22/changePassword";
        String expected9="http://127.0.0.1:22/approveJoinRequest";
        String expected10="http://127.0.0.1:22/rejectJoinRequest";
        String expected11="http://127.0.0.1:22/applyProject";
        String expected12="http://127.0.0.1:22/approveProject";
        String expected13="http://127.0.0.1:22/rejectProject";
        String expected14="http://127.0.0.1:22/getExpStatus";


        assertEquals(expected1,actual1);
        assertEquals(expected2,actual2);
        assertEquals(expected3,actual3);
        assertEquals(expected4,actual4);
        assertEquals(expected5,actual5);
        assertEquals(expected6,actual6);
        assertEquals(expected7,actual7);
        assertEquals(expected8,actual8);
        assertEquals(expected9,actual9);
        assertEquals(expected10,actual10);
        assertEquals(expected11, actual11);
        assertEquals(expected12,actual12);
        assertEquals(expected13,actual13);
        assertEquals(expected14,actual14);

    }

    @Test
    public void testGetBossUrlReturnsNotNull() throws Exception {
        assertThat(connectionProperties.getBossUrl()).isNotNull();
        assertThat(connectionProperties.getBossUrl()).isEqualTo("boss.ncl.sg");
    }

    @Test
    public void testGetUserUrlReturnsNotNull() throws Exception {
        assertThat(connectionProperties.getUserUrl()).isNotNull();
        assertThat(connectionProperties.getUserUrl()).isEqualTo("users.ncl.sg");
    }

}