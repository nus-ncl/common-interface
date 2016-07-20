package sg.ncl.service.realization.logic;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sg.ncl.adapter.deterlab.AdapterDeterlab;
import sg.ncl.adapter.deterlab.ConnectionProperties;
import sg.ncl.service.realization.AbstractTest;
import sg.ncl.service.realization.data.jpa.RealizationRepository;
import sg.ncl.service.realization.logic.RealizationService;

import javax.inject.Inject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by Desmond.
 */
public class RealizationServiceTest2 extends AbstractTest {
//    private final AdapterDeterlab adapterDeterLab = mock(AdapterDeterlab.class);
//
    @Inject
    private RealizationRepository realizationRepository;
//
//    @Before
//    public void setupTests() {
//        when(adapterDeterLab.startExperiment(any())).thenReturn("");
//        when(adapterDeterLab.stopExperiment(any())).thenReturn("");
//    }

    @Inject
    private RealizationService realizationService;






}
