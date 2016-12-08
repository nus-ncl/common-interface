package sg.ncl.service.user.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsyeoty on 08-Dec-16.
 */
public class VerificationKeyInfoTest {

    private final String key = RandomStringUtils.randomAlphanumeric(20);

    private final VerificationKeyInfo verificationKeyInfo = new VerificationKeyInfo(key);

    @Test
    public void testGetKey() throws Exception {
        assertThat(verificationKeyInfo.getKey()).isEqualTo(key);
    }
}