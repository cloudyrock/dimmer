package com.github.cloudyrock.dimmer.reader;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DimmerConfigReaderYamlImplTest {

    private DimmerConfigReader dimmerConfigReaderYamlImpl = new DimmerConfigReaderYamlImpl();

    @Test
    public void shouldLoadDimmerConfigWithCorrectFile_happyPath() {
        final DimmerConfig dimmerConfig = dimmerConfigReaderYamlImpl.fromProperties("./dimmer.yml");
        assertNotNull(dimmerConfig);
        assertNotNull(dimmerConfig.getEnvironments());
        assertThat(dimmerConfig.getEnvironments().size(), is(3));
        assertThat(dimmerConfig.getEnvironments().get("dev").getFeatureIntercept().size(), is(6));
    }

}
