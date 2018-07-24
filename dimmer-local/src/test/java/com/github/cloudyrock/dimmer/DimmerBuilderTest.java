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
        DimmerEnvironmentConfigurable obj = DimmerBuilder.local();
        assertNotNull(obj);
        final DimmerConfigurableRunner runner = obj.environments("env");
        assertTrue(runner instanceof DimmerLocalRunner);
        assertEquals(1, runner.environments.size());
        assertTrue(runner.environments.contains("env"));
    }

    @Test
    @DisplayName("Should return a DimmerRemoteRunner when call to remote()")
    public void remote() throws Exception {
        assertNotNull(DimmerBuilder.remote("URL"));
        //todo check url
    }

}