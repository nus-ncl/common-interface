package sg.ncl.service.mail.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sg.ncl.service.mail.MailApplication;

import javax.inject.Inject;

/**
 * Created by dcszwang on 8/10/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MailServiceImplTest {

    @Inject
    MailServiceImpl mail;

    @Test
    public void testSend() {
        mail.send("testbed-approval@ncl.sg", "dcszwang@nus.edu.sg", "Test", "NCL test email 2");
    }
}
