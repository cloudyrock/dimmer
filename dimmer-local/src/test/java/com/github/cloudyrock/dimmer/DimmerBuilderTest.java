package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.*;

public class DimmerBuilderTest {
    @Test
    public void local() throws Exception {
        DimmerEnvironmentConfigurable obj = DimmerBuilder.local();
        assertNotNull(obj);
        assertTrue(obj.environments("env") instanceof DimmerLocalRunner);
    }

    @Test
    public void remote() throws Exception {
        assertNotNull(DimmerBuilder.remote("URL"));
    }

}