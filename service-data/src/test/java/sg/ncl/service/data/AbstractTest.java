package sg.ncl.service.data;

import org.springframework.boot.test.context.SpringBootTest;
import sg.ncl.common.DomainProperties;
import sg.ncl.service.analytics.AnalyticsApplication;
import sg.ncl.service.mail.MailApplication;
import sg.ncl.service.transmission.UploadApplication;
import sg.ncl.service.user.UserApplication;

/**
 * Created by dcszwang on 10/6/2016.
 */
@SpringBootTest(classes = {DataApplication.class, UploadApplication.class, AnalyticsApplication.class,
        UserApplication.class, MailApplication.class, DomainProperties.class})
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
