package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import sg.ncl.service.adapter.AdapterDeterlab;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Te Ye on 15-Jun-16.
 */
public class AdapterDeterlabTest {

    @Test
    @Ignore
    public void testAddUsersOnDeter() {
        // FIXME: must have python adapter running first for test to pass
        JSONObject userObject = new JSONObject();
        userObject.put("firstname", RandomStringUtils.randomAlphanumeric(20));
        userObject.put("lastname", RandomStringUtils.randomAlphanumeric(20));
        userObject.put("password", RandomStringUtils.randomAlphanumeric(20));
        userObject.put("email", RandomStringUtils.randomAlphanumeric(8) + "@nus.edu.sg");

        final AdapterDeterlab adapterDeterlab = new AdapterDeterlab();
        String result = adapterDeterlab.addUsers(userObject.toString());
        Assert.assertThat(result,  is("user is created"));
    }

}
