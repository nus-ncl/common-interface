package sg.ncl.service.upload.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.common.exception.ExceptionAutoConfiguration;
import sg.ncl.common.exception.GlobalExceptionHandler;
import sg.ncl.service.upload.domain.UploadService;
import sg.ncl.service.upload.domain.UploadStatus;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dcsjnh on 12/2/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UploadController.class)
@ContextConfiguration(classes = {UploadController.class, ExceptionAutoConfiguration.class, GlobalExceptionHandler.class})
public class UploadControllerTest {

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @MockBean
    private UploadService uploadService;

    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(uploadService).isMock()).isTrue();
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCheckUploadUploaded() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(uploadService.checkChunk(anyString(), anyInt())).thenReturn(UploadStatus.UPLOADED);

        MvcResult result = mockMvc.perform(get(UploadController.PATH + "/chunks/1/files/identifier"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).isEqualTo("Uploaded");
    }

}
