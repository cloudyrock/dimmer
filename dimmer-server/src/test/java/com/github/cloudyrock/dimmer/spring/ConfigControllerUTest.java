package com.github.cloudyrock.dimmer.spring;


import com.github.cloudyrock.dimmer.ConfigService;
import com.github.cloudyrock.dimmer.DimmerConfigResponse;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyString;


@DisplayName("ConfigController: Unit tests")
class ConfigControllerUTest {

    @Mock
    private ConfigService configService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Nested
    @DisplayName("constructor")
    public class Constructor {

        @Test
        @DisplayName("should throw exception when constructor if parameter is null")
        void shouldReturnThrowException_whenConstructor_ifNullParameter() {
            assertThrows(IllegalArgumentException.class, () -> new ConfigController(null));

        }
    }

    @Nested
    @DisplayName("getConfigByEnvironment()")
    class GetInterceptedFeaturesByEnvironment {

        @Test
        @DisplayName("Should return list of intercepted features if environment exists")
        void shouldReturnListFeaturesIntercepted_ifEnvironmentExists() {

            //given
            final String env = "environment";
            final Set<String> expectedFeaturesIntercepted = ImmutableSet.of("feature1", "feature2");
            given(configService.getInterceptedFeaturesByEnvironment(env))
                    .willReturn(expectedFeaturesIntercepted);

            //when
            final ConfigController configController = new ConfigController(configService);
            final DimmerConfigResponse actualResponse = configController.getConfigByEnvironment(env);

            //then
            then(ConfigControllerUTest.this.configService).should().getInterceptedFeaturesByEnvironment(env);
            assertEquals(expectedFeaturesIntercepted, actualResponse.getFeaturesIntercepted());
            assertEquals(env, actualResponse.getEnvironment());
        }

        @Test
        @DisplayName("Should throw exception if environment is null")
        void shouldThrowException_ifEnvironmentIsNull() {
            given(configService.getInterceptedFeaturesByEnvironment(anyString())).willThrow(IllegalArgumentException.class);
            assertThrowsIllegalArgumentException(null);
        }

        @Test
        @DisplayName("Should throw exception if environment is empty")
        void shouldThrowException_ifEnvironmentIsEmpty() {
            given(configService.getInterceptedFeaturesByEnvironment(anyString())).willThrow(IllegalArgumentException.class);
            assertThrowsIllegalArgumentException("");
        }

        void assertThrowsIllegalArgumentException(String env) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new ConfigController(configService).getConfigByEnvironment(env)
            );

        }
    }

}