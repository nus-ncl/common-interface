
package sg.ncl.adapter.deterlab;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tran Ly Vu on 10/3/2016.
 */
public class ConnectionPropertiesTest {
    private ConnectionProperties connectionProperties;

    @Before
    public void setUp() throws Exception {
        connectionProperties=new ConnectionProperties();
    }


    @Test
    public void getterAndSetterTest(){
        connectionProperties.setIp("test1");
        connectionProperties.setPort("test2");

        String actual1=connectionProperties.getIp();
        String actual2=connectionProperties.getPort();

        assertEquals("test1",actual1);
        assertEquals("test2",actual2);
    }

    @Test
    public void othersTest(){
        connectionProperties.setIp("test1");
        connectionProperties.setPort("test2");

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


        String expected1="http://test1:test2/joinProjectNewUsers";
        String expected2="http://test1:test2/joinProject";
        String expected3="http://test1:test2/applyProjectNewUsers";
        String expected4="http://test1:test2/createExperiment";
        String expected5="http://test1:test2/startExperiment";
        String expected6="http://test1:test2/stopExperiment";
        String expected7="http://test1:test2/deleteExperiment";
        String expected8="http://test1:test2/changePassword";
        String expected9="http://test1:test2/approveJoinRequest";
        String expected10="http://test1:test2/rejectJoinRequest";
        String expected11="http://test1:test2/applyProject";
        String expected12="http://test1:test2/approveProject";
        String expected13="http://test1:test2/rejectProject";
        String expected14="http://test1:test2/getExpStatus";


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



}