package com.github.cloudyrock.dimmer.spring;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("FeatureConfig: Unit tests")
class FeatureConfigUTest {


    @Nested
    @DisplayName("environments")
    class Environments {

        @Test
        @DisplayName("Should return null if environments not set")
        void shouldReturnNullIfEnvironmentNotSet() {
            assertNull(new FeatureConfig().getEnvironments());
        }

        @Test
        @DisplayName("Should return environments set previously")
        void shouldReturnEnvironmentsSetPreviously() {
            final FeatureConfig featureConfig = new FeatureConfig();
            final Map<String, Set<String>> environments = new HashMap<>();
            featureConfig.setEnvironments(environments);
            assertEquals(environments, featureConfig.getEnvironments());
        }
    }

}