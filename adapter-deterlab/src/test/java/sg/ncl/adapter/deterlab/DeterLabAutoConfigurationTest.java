package sg.ncl.adapter.deterlab;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Tran Ly Vu on 10/3/2016.
 */
public class DeterLabAutoConfigurationTest {

    private DeterLabAutoConfiguration deterLabAutoConfiguration;

    @Before
    public void setup(){
        deterLabAutoConfiguration= new DeterLabAutoConfiguration();
    }

    @Test
    public void restTemplateTest(){
        RestTemplate rest=deterLabAutoConfiguration.restTemplate();
        assertThat(rest).isNotNull();

    }
}
