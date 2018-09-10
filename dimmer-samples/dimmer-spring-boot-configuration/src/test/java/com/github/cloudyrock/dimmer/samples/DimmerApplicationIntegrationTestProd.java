package com.github.cloudyrock.dimmer.samples;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.PROD;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ActiveProfiles(PROD)
public class DimmerApplicationIntegrationTestProd extends DimmerApplicationIntegrationTest {

    @Test
    public void testGetUsersFeatureIsActive() throws Exception {
        final ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("[]"));
    }

    @Test
    public void testConditionalBehaviourIfEndpointExistsForAddUserInProd() throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final JSONObject body = new JSONObject();
        body.put("name", "DIMMER");

        final HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        final ResponseEntity<String> response = template.postForEntity(base.toString(), entity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
