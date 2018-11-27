package com.github.cloudyrock.dimmer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("FileConfigRepositoryImpl Tests")
class FileConfigRepositoryImplTest {

    @Nested
    @DisplayName("constructor")
    public class Constructor {

        @Test
        @DisplayName("should return throw exception when constructor if paramter is null")
        void shouldReturnThrowException_whenConstructor_ifNullParameter() {
            assertThrows(IllegalArgumentException.class, () -> new FileConfigRepositoryImpl(null));

        }
    }

    @Nested
    @DisplayName("findFeaturesInterceptedByEnvironment()")
    public class FindFeaturesInterceptedByEnvironment {

        @Test
        @DisplayName("Should return empty when called if map is empty")
        void shouldReturnEmpty_whenCall_ifMapIsEmpty() {
            FileConfigRepositoryImpl rep = new FileConfigRepositoryImpl(new HashMap<>());
            assertEquals(0, rep.findFeaturesInterceptedByEnvironment("ENV").size());
        }

        @Test
        @DisplayName("Should return empty when called if environment not found")
        void shouldReturnEmpty_whenCall_ifEnvironmentNotFound() {
            Map<String, Set<String>> envFeatureMap = new HashMap<>();
            envFeatureMap.put("ENV", new HashSet<>());
            FileConfigRepositoryImpl rep = new FileConfigRepositoryImpl(envFeatureMap);
            assertEquals(0, rep.findFeaturesInterceptedByEnvironment("NOT_FOUND_ENV").size());
        }


        @Test
        @DisplayName("Should return the right set when called with the right environment")
        void shouldReturnSet_whenCall_ifRightEnvironment() {
            Map<String, Set<String>> envFeatureMap = new HashMap<>();
            HashSet<String> expected = new HashSet<>(Arrays.asList("feature1", "feature2"));
            envFeatureMap.put("ENV", expected);
            FileConfigRepositoryImpl rep = new FileConfigRepositoryImpl(envFeatureMap);
            assertEquals(expected, rep.findFeaturesInterceptedByEnvironment("ENV"));
        }
    }

}