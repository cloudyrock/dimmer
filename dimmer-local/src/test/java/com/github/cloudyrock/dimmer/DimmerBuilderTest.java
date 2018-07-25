package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.displayname.DisplayName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DimmerBuilderTest {

    @Test
    @DisplayName("Should return a non null DimmerEnvironmentConfigurable, which returns" +
                         " DimmerLocalRunner with the environment passed when call " +
                         "to local()")
    public void local() throws Exception {
        DimmerDefaultEnvironmentConfigurable envConfigurable = DimmerBuilder.local();
        assertNotNull(envConfigurable);
        final DimmerConfigurableRunner runner = envConfigurable.environments("env");
        assertTrue(runner instanceof DimmerLocalRunner);
        assertEquals(1, runner.environments.size());
        assertTrue(runner.environments.contains("env"));


        final DimmerConfigurableRunner runner2 = envConfigurable.defaultEnvironment();
        assertTrue(runner2 instanceof DimmerLocalRunner);
        assertEquals(1, runner2.environments.size());
        assertTrue(runner2.environments.contains("DEFAULT_DIMMER_ENV"));
    }

    @Test
    @DisplayName("Should return a DimmerRemoteRunner when call to remote()")
    public void remote() throws Exception {
        assertNotNull(DimmerBuilder.remote("URL"));
        //todo check url
    }

}