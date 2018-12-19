package com.github.cloudyrock.dimmer.reader;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.DisplayName;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Dimmer;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Environment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl.DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ;
import static com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl.DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY;
import static com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl.DIMMER_CONFIG_EXCEPTION_INVALID_URL;
import static com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl.DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DimmerConfigReaderYamlImplUTest {

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    @DisplayName("Happy Path: Should load dimmer configuration file when path and file structure is correct.")
    public void shouldLoadDimmerConfig_WhenCorrectFile_happyPath() {

        final DimmerConfig dimmerConfig = new DimmerConfigReaderYamlImpl(objectMapper).loadConfiguration();
        assertNotNull(dimmerConfig);
        assertNotNull(dimmerConfig.getEnvironments());
        assertThat(dimmerConfig.getEnvironments().size(), is(3));
        assertThat(dimmerConfig.getEnvironments().get("dev").getFeatureIntercept().size(), is(6));
        assertThat(dimmerConfig.getEnvironments().get("dev").isDefault(), is(true));
    }

    @Test
    @DisplayName("Should throw DimmerConfigException when failing to load config file from path.")
    public void shouldThrowDimmerConfigException_WhenFileDoesntExist() {
        final ObjectMapper mapperMock = mock(ObjectMapper.class);
        exception.expect(DimmerConfigException.class);
        exception.expectMessage(is(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ));
        new DimmerConfigReaderYamlImpl("./random.yml", mapperMock).loadConfiguration();
    }

    @Test
    @DisplayName("Should throw DimmerConfigException when Server and FeatureIntercepts are configured for same env.")
    public void shouldThrowDimmerConfigException_WhenServerAndFeaturesAreSetForSameEnvironment() throws Exception {

        final ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(mapperMock.readValue(any(File.class), any(DimmerYamlConfig.class.getClass()))).thenReturn(invalidConfigServerAndFeaturesSetUpForEnv());

        exception.expect(DimmerConfigException.class);
        exception.expectMessage(is(DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH));

        new DimmerConfigReaderYamlImpl("./dimmer.yml", mapperMock).loadConfiguration();
        verify(mapperMock, times(1)).readValue(any(File.class), any(DimmerYamlConfig.class.getClass()));
    }

    @Test
    @DisplayName("Should throw DimmerConfigException when Server is an invalid URL.")
    public void shouldThrowDimmerConfigException_WhenServerIsNotURL() throws Exception {

        final ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(mapperMock.readValue(any(File.class), any(DimmerYamlConfig.class.getClass()))).thenReturn(invalidConfigWrongServerFormat());

        exception.expect(DimmerConfigException.class);
        exception.expectMessage(is(DIMMER_CONFIG_EXCEPTION_INVALID_URL));

        new DimmerConfigReaderYamlImpl("./dimmer.yml", mapperMock).loadConfiguration();
        verify(mapperMock, times(1)).readValue(any(File.class), any(DimmerYamlConfig.class.getClass()));
    }

    @Test
    @DisplayName("Should throw DimmerConfigException when Server and features are empty for an Environment.")
    public void shouldThrowDimmerConfigExceptionWhenServerAndFeaturesEmptyForEnv() throws Exception {

        final ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(mapperMock.readValue(any(File.class), any(DimmerYamlConfig.class.getClass()))).thenReturn(invalidConfigEmptyEnv());

        exception.expect(DimmerConfigException.class);
        exception.expectMessage(is(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY));

        new DimmerConfigReaderYamlImpl("./dimmer.yml", mapperMock).loadConfiguration();
        verify(mapperMock, times(1)).readValue(any(File.class), any(DimmerYamlConfig.class.getClass()));
    }

    private static DimmerYamlConfig invalidConfigServerAndFeaturesSetUpForEnv() {

        final Dimmer dimmer = new Dimmer();
        final HashMap<String, Environment> environments = new HashMap<>();
        final Environment env1 = new Environment();
        env1.setServer("ASDF");
        env1.setFeatureIntercept(new ArrayList<>(Arrays.asList("feature1", "feature2", "feature3")));
        environments.put("env1", env1);
        dimmer.setEnvironments(environments);

        final DimmerYamlConfig dimmerYamlConfig = new DimmerYamlConfig();
        dimmerYamlConfig.setDimmer(dimmer);

        return dimmerYamlConfig;
    }

    private static DimmerYamlConfig invalidConfigWrongServerFormat() {

        final Dimmer dimmer = new Dimmer();
        final HashMap<String, Environment> environments = new HashMap<>();
        final Environment env1 = new Environment();
        env1.setServer("RANDOM");
        environments.put("env1", env1);
        dimmer.setEnvironments(environments);

        final DimmerYamlConfig dimmerYamlConfig = new DimmerYamlConfig();
        dimmerYamlConfig.setDimmer(dimmer);

        return dimmerYamlConfig;
    }

    private static DimmerYamlConfig invalidConfigEmptyEnv() {

        final Dimmer dimmer = new Dimmer();
        final HashMap<String, Environment> environments = new HashMap<>();
        final Environment env1 = new Environment();
        environments.put("env1", env1);
        dimmer.setEnvironments(environments);

        final DimmerYamlConfig dimmerYamlConfig = new DimmerYamlConfig();
        dimmerYamlConfig.setDimmer(dimmer);

        return dimmerYamlConfig;
    }

}
