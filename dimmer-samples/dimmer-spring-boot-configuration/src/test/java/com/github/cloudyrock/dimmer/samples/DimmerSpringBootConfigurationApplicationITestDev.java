package com.github.cloudyrock.dimmer.samples;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.DEV;
import static com.github.cloudyrock.dimmer.samples.controller.UserController.USERS_PATH;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@TestConfiguration(value = "DimmerConfiguration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@ActiveProfiles(DEV)
public class DimmerSpringBootConfigurationApplicationITestDev {

    @LocalServerPort
    private int port = 8080;

    private URL base;

    @Autowired
    private TestRestTemplate template;


    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + USERS_PATH);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetUsersFeatureIsActive() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_IMPLEMENTED));
    }

    @Test
    public void testAddUsersIsActive() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("name", "DIMMER");

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = template.postForEntity(base.toString(), entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo("{\"id\":1,\"name\":\"DIMMER\"}"));
    }
}
