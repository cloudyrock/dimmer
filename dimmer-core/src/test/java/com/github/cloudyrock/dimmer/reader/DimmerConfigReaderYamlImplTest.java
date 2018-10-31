package com.github.cloudyrock.dimmer.reader;


import com.github.cloudyrock.dimmer.DimmerConfigException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl.DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ;
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
        assertThat(dimmerConfig.getEnvironments().get("dev").isDefault(), is(true));
    }


    @Test
    public void shouldThrowDimmerConfigExceptionWhenFileDoesntExist() {
        try {
            dimmerConfigReaderYamlImpl.fromProperties("./random.yml");
            assert (false);
        } catch (DimmerConfigException e) {
            assertThat(e.getMessage(), is(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ));
        }
    }

    @Test
    public void shouldThrowDimmerConfigExceptionWhenConfigIsInvalid() {
        try {
            dimmerConfigReaderYamlImpl.fromProperties("./dimmer-invalid.yml");
            assert (false);
        } catch (DimmerConfigException e) {
            assertThat(e.getMessage(), is(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ));
        }
    }

}
