package sg.ncl.service.data;

import org.springframework.boot.test.context.SpringBootTest;
import sg.ncl.service.upload.UploadApplication;

/**
 * Created by dcszwang on 10/6/2016.
 */
@SpringBootTest(classes = {DataApplication.class, UploadApplication.class})
public abstract class AbstractTest extends sg.ncl.common.test.AbstractTest {}
