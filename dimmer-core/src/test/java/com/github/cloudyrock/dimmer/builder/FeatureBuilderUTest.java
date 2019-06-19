package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.DisplayName;
import com.github.cloudyrock.dimmer.exception.DimmerConfigException;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FeatureBuilderUTest {

    private static final String ENV = "ENV";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DimmerConfigReader dimmerConfigReaderMock;

    @Before
    public void setUp() {
        initMocks(this);
    }


    //TODO test this also in ConfigReaderUTest
    @Test
    @DisplayName("Should throw exception when DimmerReader throws exception loading environment")
    public void shouldThrowException_WhenDimmerReaderExceptionIsThrown() {

        when(dimmerConfigReaderMock.loadEnvironmentOrDefault(anyString())).thenThrow(new DimmerConfigException("MESSAGE"));
        exception.expect(DimmerConfigException.class);
        exception.expectMessage("MESSAGE");

        new FeatureBuilder(Collections.singleton(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .run("RANDOM_ENV");
    }

    @Test
    @DisplayName("HappyPath: should work when building a specific existing environment set up.")
    public void happyPath() {

        when(dimmerConfigReaderMock.loadEnvironmentOrDefault(anyString())).thenReturn(loadDimmerConfig());

        new FeatureBuilder(Collections.singletonList(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .run(ENV);
    }

    @Test
    @DisplayName("HappyPath: should work for default environment set up")
    public void happyPathWithDefaultEnvironment() {

        when(dimmerConfigReaderMock.loadEnvironmentOrDefault(anyString())).thenReturn(loadDimmerConfig());
        when(dimmerConfigReaderMock.loadEnvironmentOrDefault(anyString())).thenReturn(loadDefaultEntry());

        new FeatureBuilder(Collections.singleton(ENV), new HashMap<>(), dimmerConfigReaderMock)
                .runWithDefaultEnvironment();
    }

    //TODO add this to integration tests
//    @Test(expected = DimmerInvocationException.class)
//    @DisplayName("Should throw error when building default environment doesn't exist.")
//    public void shouldThrowException_WhenDefaultEnvironmentDoesntExist() {
//
//        when(dimmerConfigReaderMock.loadConfiguration()).thenReturn(loadDimmerConfigWithoutDefaultEnv());
//        when(dimmerConfigReaderMock.getDefaultEnvironment(any(DimmerConfig.class))).thenThrow(new DimmerConfigException(""));
//
//        FeatureBuilder
//                .withEnvironmentsAndMetadata(Collections.singletonList(ENV), new HashMap<>(), dimmerConfigReaderMock)
//                .runWithDefaultEnvironment();
//    }


    private static EnvironmentConfig loadDimmerConfig() {
        return new EnvironmentConfig(ENV, null, Arrays.asList("Feature1", "Feature2"), true);
    }

    private static DimmerConfig loadDimmerConfigWithoutDefaultEnv() {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap = new HashMap<>();
        environmentConfigMap.put(ENV, new EnvironmentConfig(ENV, null, Arrays.asList("Feature1", "Feature2"), false));
        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

    private static EnvironmentConfig loadDefaultEntry() {
        return new EnvironmentConfig(ENV, null, Arrays.asList("Feature1", "Feature2"), true);
    }


}