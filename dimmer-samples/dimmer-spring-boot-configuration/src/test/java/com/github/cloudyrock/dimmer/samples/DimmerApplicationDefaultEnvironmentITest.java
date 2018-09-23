package com.github.cloudyrock.dimmer.samples;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.DEFAULT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@EnableAutoConfiguration
@ActiveProfiles(DEFAULT)
public class DimmerApplicationDefaultEnvironmentITest extends DimmerApplicationITest {

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
