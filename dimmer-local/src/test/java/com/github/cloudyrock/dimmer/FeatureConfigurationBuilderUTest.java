package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.cloudyrock.dimmer.FeatureConfigurationBuilder.DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FeatureConfigurationBuilderUTest {

    private static final String ENV = "ENV";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DimmerConfigReader dimmerConfigReaderMock;

    @Before
    public void setUp() {
        initMocks(this);
    }


    @Test
    @DisplayName("Should throw DimmerConfigException when build environment doesn't exist in config file")
    public void shouldThrowDimmerConfigException_WhenEnvironmentDoesntExistInConfig() {

        when(dimmerConfigReaderMock.loadConfiguration()).thenReturn(loadDimmerConfig());
        exception.expect(DimmerConfigException.class);
        exception.expectMessage(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE);

        FeatureConfigurationBuilder
                .withEnvironmentsAndMetadata(Arrays.asList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .build("RANDOM_ENV");
    }

    @Test
    @DisplayName("HappyPath: should ")
    public void shouldThrowException_WhenDimmerReaderExceptionIsThrown() {

        final String test = "TEST";
        when(dimmerConfigReaderMock.loadConfiguration()).thenThrow(new DimmerConfigException(test));
        exception.expect(DimmerConfigException.class);
        exception.expectMessage(test);

        FeatureConfigurationBuilder
                .withEnvironmentsAndMetadata(Arrays.asList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .build(ENV);
    }

    @Test
    @DisplayName("HappyPath: should work when building a specific existing environment set up.")
    public void happyPath() {

        when(dimmerConfigReaderMock.loadConfiguration()).thenReturn(loadDimmerConfig());

        FeatureConfigurationBuilder
                .withEnvironmentsAndMetadata(Arrays.asList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .build(ENV);
    }

    @Test
    @DisplayName("HappyPath: should work for default environment set up")
    public void happyPathWithDefaultEnvironment() {

        when(dimmerConfigReaderMock.loadConfiguration()).thenReturn(loadDimmerConfig());
        when(dimmerConfigReaderMock.getDefaultEnvironment(any(DimmerConfig.class))).thenReturn(loadDefaultEntry());

        FeatureConfigurationBuilder
                .withEnvironmentsAndMetadata(Arrays.asList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .buildWithDefaultEnvironment();
    }

    @Test
    @DisplayName("Should throw error when building default environment doesn't exist.")
    public void shouldThrowException_WhenDefaultEnvironmentDoesntExist() {

        when(dimmerConfigReaderMock.loadConfiguration()).thenReturn(loadDimmerConfigWithoutDefaultEnv());
        when(dimmerConfigReaderMock.getDefaultEnvironment(any(DimmerConfig.class))).thenThrow(new DimmerConfigException(""));
        exception.expect(DimmerConfigException.class);

        FeatureConfigurationBuilder
                .withEnvironmentsAndMetadata(Arrays.asList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .buildWithDefaultEnvironment();
    }


    private static DimmerConfig loadDimmerConfig() {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap = new HashMap<>();
        environmentConfigMap.put(ENV, new EnvironmentConfig(null, Arrays.asList("Feature1", "Feature2"), true));
        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

    private static DimmerConfig loadDimmerConfigWithoutDefaultEnv() {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap = new HashMap<>();
        environmentConfigMap.put(ENV, new EnvironmentConfig(null, Arrays.asList("Feature1", "Feature2"), false));
        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

    private static Map.Entry<String,EnvironmentConfig> loadDefaultEntry(){
        return new Map.Entry<String, EnvironmentConfig>() {
            @Override
            public String getKey() {
                return ENV;
            }

            @Override
            public EnvironmentConfig getValue() {
                return new EnvironmentConfig(null, Arrays.asList("Feature1", "Feature2"), true);
            }

            @Override
            public EnvironmentConfig setValue(EnvironmentConfig value) {
                return null;
            }
        };
    }


}