package sg.ncl.common.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.security.Key;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

        assertThat(jwtFilter).isNotNull();
    }

    @Test
    public void testJwtParser() throws Exception {
        final Key key = mock(Key.class);

        final JwtParser jwtParser = configuration.jwtParser(key);

        assertThat(jwtParser).isNotNull();
    }

    @Test
    public void testSignatureAlgorithm() throws Exception {
        doReturn(DEFAULT_SIGNATURE_ALGORITHM.getValue()).when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm).isNotNull();
        assertThat(algorithm.getValue()).isEqualTo(DEFAULT_SIGNATURE_ALGORITHM.getValue());
    }

    @Test
    public void testSignatureAlgorithmWithNullSigningAlgorithm() throws Exception {
        doReturn(null).when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm).isNotNull();
        assertThat(algorithm.getValue()).isEqualTo(DEFAULT_SIGNATURE_ALGORITHM.getValue());
    }

    @Test
    public void testSignatureAlgorithmWithEmptySigningAlgorithm() throws Exception {
        doReturn("").when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm).isNotNull();
        assertThat(algorithm.getValue()).isEqualTo(DEFAULT_SIGNATURE_ALGORITHM.getValue());
    }

    @Test
    public void testSignatureAlgorithmWithUnknownSigningAlgorithm() throws Exception {
        doReturn("unknown").when(properties).getSigningAlgorithm();

        final SignatureAlgorithm algorithm = configuration.signatureAlgorithm();

        assertThat(algorithm).isNotNull();
        assertThat(algorithm.getValue()).isEqualTo(DEFAULT_SIGNATURE_ALGORITHM.getValue());
    }

    @Test
    public void testKey() throws Exception {
        final String s = "123";
        doReturn(s).when(properties).getApiKey();

        final Key key = configuration.apiKey(DEFAULT_SIGNATURE_ALGORITHM);

        assertThat(key).isNotNull();
        assertThat(key.getEncoded()).isEqualTo(s.getBytes());
    }

    @Test
    public void testKeyWithNullApiKey() throws Exception {
        doReturn(null).when(properties).getApiKey();

        final Key key = configuration.apiKey(DEFAULT_SIGNATURE_ALGORITHM);

        assertThat(key).isNotNull();
        assertThat(key.getEncoded()).isEqualTo(DEFAULT_API_KEY.toString().getBytes());
    }

    @Test
    public void testKeyWithEmptyApiKey() throws Exception {
        doReturn("").when(properties).getApiKey();

        final Key key = configuration.apiKey(DEFAULT_SIGNATURE_ALGORITHM);

        assertThat(key).isNotNull();
        assertThat(key.getEncoded()).isEqualTo(DEFAULT_API_KEY.toString().getBytes());
    }

    @Test
    public void testDuration() throws Exception {
        final Duration d = Duration.ofHours(2);
        doReturn(d.toString()).when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration).isNotNull();
        assertThat(duration).isEqualTo(d);
    }

    @Test
    public void testDurationWithNullExpiryDuration() throws Exception {
        doReturn(null).when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration).isNotNull();
        assertThat(duration).isEqualTo(DEFAULT_EXPIRY_DURATION);
    }

    @Test
    public void testDurationWithEmptyExpiryDuration() throws Exception {
        doReturn("").when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration).isNotNull();
        assertThat(duration).isEqualTo(DEFAULT_EXPIRY_DURATION);
    }

    @Test
    public void testDurationWithInvalidExpiryDuration() throws Exception {
        doReturn("invalid").when(properties).getExpiryDuration();

        final Duration duration = configuration.expiryDuration();

        assertThat(duration).isNotNull();
        assertThat(duration).isEqualTo(DEFAULT_EXPIRY_DURATION);
    }

}
