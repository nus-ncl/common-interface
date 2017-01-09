
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
@SpringBootTest(classes = TestConfig.class, webEnvironment = WebEnvironment.NONE)
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
    public void testGetJoinProjectNewUsers() throws Exception {
        assertThat(connectionProperties.getJoinProjectNewUsers()).isNotNull();
        assertThat(connectionProperties.getJoinProjectNewUsers()).isEqualTo("http://127.0.0.1:22/joinProjectNewUsers");
    }

    @Test
    public void testGetJoinProject() throws Exception {
        assertThat(connectionProperties.getJoinProject()).isNotNull();
        assertThat(connectionProperties.getJoinProject()).isEqualTo("http://127.0.0.1:22/joinProject");
    }

    @Test
    public void testGetApplyProjectNewUsers() throws Exception {
        assertThat(connectionProperties.getApplyProjectNewUsers()).isNotNull();
        assertThat(connectionProperties.getApplyProjectNewUsers()).isEqualTo("http://127.0.0.1:22/applyProjectNewUsers");
    }

    @Test
    public void testGetCreateExperiment() throws Exception {
        assertThat(connectionProperties.getCreateExperiment()).isNotNull();
        assertThat(connectionProperties.getCreateExperiment()).isEqualTo("http://127.0.0.1:22/createExperiment");
    }

    @Test
    public void testStartExperiment() throws Exception {
        assertThat(connectionProperties.startExperiment()).isNotNull();
        assertThat(connectionProperties.startExperiment()).isEqualTo("http://127.0.0.1:22/startExperiment");
    }

    @Test
    public void testStopExperiment() throws Exception {
        assertThat(connectionProperties.stopExperiment()).isNotNull();
        assertThat(connectionProperties.stopExperiment()).isEqualTo("http://127.0.0.1:22/stopExperiment");
    }

    @Test
    public void testDeleteExperiment() throws Exception {
        assertThat(connectionProperties.deleteExperiment()).isNotNull();
        assertThat(connectionProperties.deleteExperiment()).isEqualTo("http://127.0.0.1:22/deleteExperiment");
    }

    @Test
    public void testGetUpdateCredentials() throws Exception {
        assertThat(connectionProperties.getUpdateCredentials()).isNotNull();
        assertThat(connectionProperties.getUpdateCredentials()).isEqualTo("http://127.0.0.1:22/changePassword");
    }

    @Test
    public void testGetApproveJoinRequest() throws Exception {
        assertThat(connectionProperties.getApproveJoinRequest()).isNotNull();
        assertThat(connectionProperties.getApproveJoinRequest()).isEqualTo("http://127.0.0.1:22/approveJoinRequest");
    }

    @Test
    public void testGetRejectJoinRequest() throws Exception {
        assertThat(connectionProperties.getRejectJoinRequest()).isNotNull();
        assertThat(connectionProperties.getRejectJoinRequest()).isEqualTo("http://127.0.0.1:22/rejectJoinRequest");
    }

    @Test
    public void testGetApplyProject() throws Exception {
        assertThat(connectionProperties.getApplyProject()).isNotNull();
        assertThat(connectionProperties.getApplyProject()).isEqualTo("http://127.0.0.1:22/applyProject");
    }

    @Test
    public void testGetApproveProject() throws Exception {
        assertThat(connectionProperties.getApproveProject()).isNotNull();
        assertThat(connectionProperties.getApproveProject()).isEqualTo("http://127.0.0.1:22/approveProject");
    }

    @Test
    public void testGetRejectProject() throws Exception {
        assertThat(connectionProperties.getRejectProject()).isNotNull();
        assertThat(connectionProperties.getRejectProject()).isEqualTo("http://127.0.0.1:22/rejectProject");
    }

    @Test
    public void testGetExpStatus() throws Exception {
        assertThat(connectionProperties.getExpStatus()).isNotNull();
        assertThat(connectionProperties.getExpStatus()).isEqualTo("http://127.0.0.1:22/getExpStatus");
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

    @Test
    public void testLogin() throws Exception {
        assertThat(connectionProperties.login()).isNotNull();
        assertThat(connectionProperties.login()).isEqualTo("http://127.0.0.1:22/login");
    }

    @Test
    public void testGetTopoThumbnail() throws Exception {
        assertThat(connectionProperties.getTopoThumbnail()).isNotNull();
        assertThat(connectionProperties.getTopoThumbnail()).isEqualTo("http://127.0.0.1:22/getTopoThumbnail");
    }

    @Test
    public void testGetUsageStatistics() throws Exception {
        assertThat(connectionProperties.getUsageStatistics()).isNotNull();
        assertThat(connectionProperties.getUsageStatistics()).isEqualTo("http://127.0.0.1:22/getUsageStatistics");
    }

    @Test
    public void testGetResetPasswordURI() throws Exception {
        assertThat(connectionProperties.getResetPasswordURI()).isNotNull();
        assertThat(connectionProperties.getResetPasswordURI()).isEqualTo("http://127.0.0.1:22/resetPassword");
    }

    @Test
    public void testGetGlobalImages() throws Exception {
        assertThat(connectionProperties.getGlobalImages()).isNotNull();
        assertThat(connectionProperties.getGlobalImages()).isEqualTo("http://127.0.0.1:22/getCveImages");
    }


    @Test
    public void testGetSavedImages() throws Exception {
        assertThat(connectionProperties.getSavedImages()).isNotNull();
        assertThat(connectionProperties.getSavedImages()).isEqualTo("http://127.0.0.1:22/getSavedImages");
    }

    @Test
    public void testSaveImage() throws Exception {
        assertThat(connectionProperties.saveImage()).isNotNull();
        assertThat(connectionProperties.saveImage()).isEqualTo("http://127.0.0.1:22/saveImage");
    }

    @Test
    public void testGetFreeNodes() throws Exception {
        assertThat(connectionProperties.getFreeNodes()).isNotNull();
        assertThat(connectionProperties.getFreeNodes()).isEqualTo("http://127.0.0.1:22/getFreeNodes");
    }

    @Test
    public void testGetTotalNodes() throws Exception {
        assertThat(connectionProperties.getTotalNodes()).isNotNull();
        assertThat(connectionProperties.getTotalNodes()).isEqualTo("http://127.0.0.1:22/getTotalNodes");
    }

    @Test
    public void testRemoveUserFromTeam() throws Exception {
        assertThat(connectionProperties.removeUserFromTeam()).isNotNull();
        assertThat(connectionProperties.removeUserFromTeam()).isEqualTo("http://127.0.0.1:22/removeUserFromTeam");
    }

}