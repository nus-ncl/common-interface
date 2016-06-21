package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import sg.ncl.adapter.deterlab.data.jpa.DeterlabUserRepository;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Te Ye on 15-Jun-16.
 */
public class AdapterDeterlabTest extends AbstractTest {

    @Inject
    private DeterlabUserRepository deterlabUserRepository;

    @Test
    @Ignore
    public void testAddUsersOnDeter() {
        // FIXME: must have python adapter running first for test to pass
        JSONObject userObject = Util.getUserAdapterJSONObject();

        final AdapterDeterlab adapterDeterlab = new AdapterDeterlab();
        String result = adapterDeterlab.addUsers(userObject.toString());
        JSONObject resultJSONObject = new JSONObject(result);
        String msg = resultJSONObject.getString("msg");
        String uid = resultJSONObject.getString("uid");
        Assert.assertThat(msg, is("user is created"));
        Assert.assertThat(uid, not(nullValue()));
    }

    @Test
    public void testSaveUserOnDeterUserRepository() {
        String nclUserId = RandomStringUtils.randomAlphanumeric(20);
        String deterUserId = RandomStringUtils.randomAlphanumeric(8);
        AdapterDeterlab adapterDeterlab = new AdapterDeterlab(deterlabUserRepository);

        adapterDeterlab.saveDeterUserIdMapping(deterUserId, nclUserId);
        Assert.assertThat(deterlabUserRepository.findByDeterUserId(deterUserId), not(nullValue()));
        Assert.assertThat(deterUserId, is(deterlabUserRepository.findByDeterUserId(deterUserId).getDeterUserId()));
    }

}
