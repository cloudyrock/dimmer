package com.github.cloudyrock.dimmer.samples;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.net.URL;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.DEFAULT;
import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.DEV;
import static com.github.cloudyrock.dimmer.samples.controller.UserController.USERS_PATH;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ActiveProfiles(DEFAULT)
public class DimmerApplicationIntegrationTestDefault extends DimmerApplicationIntegrationTest {

    @Test
    public void testGetUsersFeatureIsActive() throws Exception {
        final ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testAddUsersIsActiveWithDefaultConfiguration() throws Exception {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final JSONObject body = new JSONObject();
        body.put("name", "DIMMER");

        final HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        final ResponseEntity<String> response = template.postForEntity(base.toString(), entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo("{\"id\":1,\"name\":\"DIMMER\"}"));
    }
}
