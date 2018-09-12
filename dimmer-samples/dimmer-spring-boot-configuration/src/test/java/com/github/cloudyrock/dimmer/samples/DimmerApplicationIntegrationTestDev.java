package com.github.cloudyrock.dimmer.samples;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.DEV;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@EnableAutoConfiguration
@ActiveProfiles(DEV)
public class DimmerApplicationIntegrationTestDev extends DimmerApplicationIntegrationTest {

    @Test
    public void testGetUsersFeatureIsThrowsCustomException() throws Exception {
        final ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_IMPLEMENTED));
    }

    @Test
    public void testAddUsersRespondsMockedValueInDev() throws Exception {

        final String mockedResponse = "MOCKED VALUE";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("name", "DIMMER");

        final HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        final ResponseEntity<String> response = template.postForEntity(base.toString(), entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo("{\"id\":1,\"name\":\"" + mockedResponse + "\"}"));
    }
}
