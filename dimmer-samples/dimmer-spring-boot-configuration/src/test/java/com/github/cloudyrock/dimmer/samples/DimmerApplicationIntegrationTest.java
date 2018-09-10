package com.github.cloudyrock.dimmer.samples;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static com.github.cloudyrock.dimmer.samples.controller.UserController.USERS_PATH;

@RunWith(SpringRunner.class)
@TestConfiguration(value = "DimmerConfiguration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class DimmerApplicationIntegrationTest {

    @LocalServerPort
    private int port = 8080;

    protected URL base;

    @Autowired
    protected TestRestTemplate template;


    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + USERS_PATH);
    }
}
