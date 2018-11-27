package com.github.cloudyrock.dimmer;

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

@DisplayName("ConfigService: Unit tests")
class ConfigServiceUTest {

    @Mock
    private ConfigRepository configRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Nested
    @DisplayName("constructor")
    class Constructor {

        @Test
        @DisplayName("should throw exception when constructor if parameter is null")
        void shouldReturnThrowException_whenConstructor_ifNullParameter() {
            assertThrows(IllegalArgumentException.class, () -> new ConfigService(null));

        }
    }

    @Nested
    @DisplayName("getInterceptedFeaturesByEnvironment()")
    class GetInterceptedFeaturesByEnvironment {

        @Test
        @DisplayName("Should return list of intercepted features if environment exists")
        void shouldReturnListFeaturesIntercepted_ifEnvironmentExists() {

            //given
            final String env = "environment";
            final Set<String> expectedFeaturesIntercepted = ImmutableSet.of("feature1", "feature2");
            given(configRepository.findFeaturesInterceptedByEnvironment(env))
                    .willReturn(expectedFeaturesIntercepted);

            //when
            final ConfigService configService = new ConfigService(configRepository);
            final Set<String> actualFeaturesIntercepted = configService.getInterceptedFeaturesByEnvironment(env);

            //then
            then(configRepository).should().findFeaturesInterceptedByEnvironment(env);
            assertEquals(expectedFeaturesIntercepted, actualFeaturesIntercepted);

        }

        @Test
        @DisplayName("Should throw exception if environment is null")
        void shouldThrowException_ifEnvironmentIsNull() {
            assertThrowsIllegalArgumentException(null);
        }

        @Test
        @DisplayName("Should throw exception if environment is empty")
        void shouldThrowException_ifEnvironmentIsEmpty() {
            assertThrowsIllegalArgumentException("");
        }

        void assertThrowsIllegalArgumentException(String env) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new ConfigService(configRepository).getInterceptedFeaturesByEnvironment(env)
            );

        }
    }



}