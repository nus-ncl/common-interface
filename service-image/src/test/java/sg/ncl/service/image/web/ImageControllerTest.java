package sg.ncl.service.image.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sg.ncl.service.image.data.jpa.ImageEntity;
import sg.ncl.service.image.domain.Image;
import sg.ncl.service.image.domain.ImageService;
import sg.ncl.service.image.domain.ImageVisibility;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static sg.ncl.service.image.util.TestUtil.getImageEntity;

/**
 * Created by dcsyeoty on 30-Oct-16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ImageController.class, secure = true)
@ContextConfiguration(classes = {ImageController.class})
public class ImageControllerTest {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Mock
    private Claims claims;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Before
    public void before() {
        assertThat(mockingDetails(claims).isMock()).isTrue();
        assertThat(mockingDetails(securityContext).isMock()).isTrue();
        assertThat(mockingDetails(authentication).isMock()).isTrue();
        assertThat(mockingDetails(imageService).isMock()).isTrue();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddImage() throws Exception {

        final ImageInfo imageInfo =  new ImageInfo(1L, "teamId", "imageName", "nodeId", "description", "currentOS", ImageVisibility.PUBLIC);
        final byte[] content = mapper.writeValueAsBytes(imageInfo);

        final ImageEntity entity = new ImageEntity();
        entity.setId(1L);
        entity.setTeamId("teamId");
        entity.setImageName("imageName");
        entity.setNodeId("nodeId");
        entity.setDescription("description");
        entity.setVisibility(ImageVisibility.PUBLIC);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(claims);
        when(imageService.addImage(any(Image.class), any(Claims.class))).thenReturn(entity);

        mockMvc.perform(post(ImageController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(equalTo(1))));
    }

    @Test
    public void testAddImageInvalidAuthenticationPrincipal() throws Exception {
        final ImageInfo imageInfo =  new ImageInfo(1L, "teamId", "imageName", "nodeId", "description", "currentOS", ImageVisibility.PUBLIC);
        final byte[] content = mapper.writeValueAsBytes(imageInfo);

        final ImageEntity entity = new ImageEntity();
        entity.setId(1L);
        entity.setTeamId("teamId");
        entity.setImageName("imageName");
        entity.setNodeId("nodeId");
        entity.setDescription("description");
        entity.setVisibility(ImageVisibility.PUBLIC);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(null);
        when(imageService.addImage(any(Image.class), any(Claims.class))).thenReturn(entity);

        mockMvc.perform(post(ImageController.PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        final List<ImageEntity> list = new ArrayList<>();
        final ImageEntity entity1 = getImageEntity();
        final ImageEntity entity2 = getImageEntity();
        list.add(entity1);
        list.add(entity2);

        doReturn(list).when(imageService).getAll(anyString(), any());

        mockMvc.perform(get(ImageController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(equalTo(entity1.getId()))))
                .andExpect(jsonPath("$[0].teamId", is(equalTo(entity1.getTeamId()))))
                .andExpect(jsonPath("$[0].imageName", is(equalTo(entity1.getImageName()))))
                .andExpect(jsonPath("$[0].nodeId", is(equalTo(entity1.getNodeId()))))
                .andExpect(jsonPath("$[0].description", is(equalTo(entity1.getDescription()))))
                .andExpect(jsonPath("$[0].visibility", is(equalTo(entity1.getVisibility().toString()))))

                .andExpect(jsonPath("$[1].id", is(equalTo(entity2.getId()))))
                .andExpect(jsonPath("$[1].teamId", is(equalTo(entity2.getTeamId()))))
                .andExpect(jsonPath("$[1].imageName", is(equalTo(entity2.getImageName()))))
                .andExpect(jsonPath("$[1].nodeId", is(equalTo(entity2.getNodeId()))))
                .andExpect(jsonPath("$[1].description", is(equalTo(entity2.getDescription()))))
                .andExpect(jsonPath("$[1].visibility", is(equalTo(entity2.getVisibility().toString()))));
    }

    @Test
    public void testGetImage() throws Exception {
        final ImageEntity entity = getImageEntity();

        when(imageService.getImage(anyLong())).thenReturn(entity);

        mockMvc.perform(get(ImageController.PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(equalTo(entity.getId()))))
                .andExpect(jsonPath("$.teamId", is(equalTo(entity.getTeamId()))))
                .andExpect(jsonPath("$.imageName", is(equalTo(entity.getImageName()))))
                .andExpect(jsonPath("$.nodeId", is(equalTo(entity.getNodeId()))))
                .andExpect(jsonPath("$.description", is(equalTo(entity.getDescription()))))
                .andExpect(jsonPath("$.visibility", is(equalTo(entity.getVisibility().toString()))));
    }

    @Test
    public void testGetGlobalImages() throws Exception {
        Map<String, String> result = new HashMap<>();
        result.put("images", "{imageA: {os: Linux, description: image description}}");

        when(imageService.getGlobalImages()).thenReturn(result);

        mockMvc.perform(get(ImageController.PATH + "/global"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.images", is(equalTo("{imageA: {os: Linux, description: image description}}"))));
    }

    @Test
    public void testGetSavedImages() throws Exception {
        Map<String, String> result = new HashMap<>();
        result.put("teamId", "{imageA:created}");

        when(imageService.getSavedImages(anyString())).thenReturn(result);

        mockMvc.perform(get(ImageController.PATH + "/teams/teamId"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.teamId", is(equalTo("{imageA:created}"))));
    }

}
