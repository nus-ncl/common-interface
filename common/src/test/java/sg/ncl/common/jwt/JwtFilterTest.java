package sg.ncl.common.jwt;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Te Ye, Christopher Zhong
 * @version 1.0
 */
public class JwtFilterTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private JwtFilter jwtFilter = new JwtFilter();

    @Before
    public void before() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testDoInternalFilter() throws Exception {
        final String s = "AAA.BBB.CCC";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(JwtFilter.BEARER + s);

        jwtFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).isEqualTo(s);
    }

    @Test
    public void testDoInternalFilterNullAuthorizationHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testDoInternalFilterEmptyAuthorizationHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

        jwtFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testDoInternalFilterUnknownAuthorizationHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("unknown");

        jwtFilter.doFilter(request, response, filterChain);
        
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

}
