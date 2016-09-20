package sg.ncl.common.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.Key;
import java.time.Duration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static sg.ncl.common.jwt.JwtAutoConfiguration.DEFAULT_API_KEY;
import static sg.ncl.common.jwt.JwtAutoConfiguration.DEFAULT_EXPIRY_DURATION;
import static sg.ncl.common.jwt.JwtAutoConfiguration.DEFAULT_SIGNATURE_ALGORITHM;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public class JwtAutoConfigurationTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private JwtProperties properties;

    private JwtAutoConfiguration configuration;

    @Before
    public void before() {
        configuration = new JwtAutoConfiguration(properties);
    }

    @Test
    public void testJwtFilter() throws Exception {
        final JwtFilter jwtFilter = configuration.jwtFilter();

        assertThat(jwtFilter, is(not(nullValue(JwtFilter.class))));
    }

    @Test
    public void testSignatureAlgorithm() throws Exception {
        doReturn(DEFAULT_SIGNATURE_ALGORITHM.getValue()).when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm, is(not(nullValue(SignatureAlgorithm.class))));
        assertThat(algorithm.getValue(), is(equalTo(DEFAULT_SIGNATURE_ALGORITHM.getValue())));
    }

    @Test
    public void testSignatureAlgorithmWithNullSigningAlgorithm() throws Exception {
        doReturn(null).when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm, is(not(nullValue(SignatureAlgorithm.class))));
        assertThat(algorithm.getValue(), is(equalTo(DEFAULT_SIGNATURE_ALGORITHM.getValue())));
    }

    @Test
    public void testSignatureAlgorithmWithEmptySigningAlgorithm() throws Exception {
        doReturn("").when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm, is(not(nullValue(SignatureAlgorithm.class))));
        assertThat(algorithm.getValue(), is(equalTo(DEFAULT_SIGNATURE_ALGORITHM.getValue())));
    }

    @Test
    public void testSignatureAlgorithmWithUnknownSigningAlgorithm() throws Exception {
        doReturn("unknown").when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm, is(not(nullValue(SignatureAlgorithm.class))));
        assertThat(algorithm.getValue(), is(equalTo(DEFAULT_SIGNATURE_ALGORITHM.getValue())));
    }

    @Test
    public void testKey() throws Exception {
        final String s = "123";
        doReturn(s).when(properties).getApiKey();

        final Key key = configuration.apiKey(configuration.signatureAlgorithm());

        assertThat(key, is(not(nullValue(Key.class))));
        assertThat(key.getEncoded(), is(equalTo(s.getBytes())));
    }

    @Test
    public void testKeyWithNullApiKey() throws Exception {
        doReturn(null).when(properties).getApiKey();

        final Key key = configuration.apiKey(configuration.signatureAlgorithm());

        assertThat(key, is(not(nullValue(Key.class))));
        assertThat(key.getEncoded(), is(equalTo(DEFAULT_API_KEY.toString().getBytes())));
    }

    @Test
    public void testKeyWithEmptyApiKey() throws Exception {
        doReturn("").when(properties).getApiKey();

        final Key key = configuration.apiKey(configuration.signatureAlgorithm());

        assertThat(key, is(not(nullValue(Key.class))));
        assertThat(key.getEncoded(), is(equalTo(DEFAULT_API_KEY.toString().getBytes())));
    }

    @Test
    public void testDuration() throws Exception {
        final String s = "PT43H";
        doReturn(s).when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration, is(not(nullValue(Duration.class))));
        assertThat(duration.toString(), is(equalTo(s)));
    }

    @Test
    public void testDurationWithNullExpiryDuration() throws Exception {
        doReturn(null).when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration, is(not(nullValue(Duration.class))));
        assertThat(duration, is(equalTo(DEFAULT_EXPIRY_DURATION)));
    }

    @Test
    public void testDurationWithEmptyExpiryDuration() throws Exception {
        doReturn("").when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration, is(not(nullValue(Duration.class))));
        assertThat(duration, is(equalTo(DEFAULT_EXPIRY_DURATION)));
    }

    @Test
    public void testDurationWithInvalidExpiryDuration() throws Exception {
        doReturn("invalid").when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration, is(not(nullValue(Duration.class))));
        assertThat(duration, is(equalTo(DEFAULT_EXPIRY_DURATION)));
    }

}
