package sg.ncl.service.adapter;

import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is to invoke python scripts on the BOSS
 * Created by Te Ye on 15-Jun-16.
 */
public class AdapterDeterlab {

    private RestTemplate restTemplate = new RestTemplate();
    private ConnectionProperties properties = new ConnectionProperties();

    public String addUsers(String jsonString) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity responseEntity = restTemplate.exchange(properties.getADD_USERS_URI(), HttpMethod.POST, request, String.class);

        // Will return the following:
        // no user created
        // user is created
        // user not found
        return responseEntity.getBody().toString();
    }
}
