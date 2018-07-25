package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.displayname.DisplayName;
import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class DimmerLocalRunnerTest {

    @Test
    @DisplayName("Constructors")
    public void constructors() {
        final Collection<String> envs = new HashSet<>();
        final Map<String, Set<FeatureMetadata>> configMetadata = new HashMap<>();

        final DimmerLocalRunner runner = DimmerLocalRunner
                .withEnvsAndMetadata(envs, configMetadata);
        assertEquals(envs, runner.environments);
        assertEquals(configMetadata, runner.configMetadata);
        assertEquals(DimmerInvocationException.class, runner.defaultExceptionType);

        Class<? extends RuntimeException> exceptionType = RuntimeException.class;
        final DimmerLocalRunner runner2 = DimmerLocalRunner
                .withEnvsMetadataAndException(envs, configMetadata, exceptionType);
        assertEquals(envs, runner2.environments);
        assertEquals(configMetadata, runner2.configMetadata);
        assertEquals(exceptionType, runner2.defaultExceptionType);
    }

    @Test
    @DisplayName("Default environment")
    public void defaultEnvironment() {

        final String feature1 = "feature1";
        final String feature2 = "feature2";
        final String feature3 = "feature3";
        final String feature4 = "feature4";
        final Function<FeatureInvocation, ?> behaviour = FeatureInvocation::getMethodName;
        final Object value = "VALUE";
        final Class<? extends RuntimeException> exceptionType = RuntimeException.class;
        final DimmerLocalRunner runner = DimmerLocalRunner
                .withDefaultEnviroment()
                .featureWithBehaviour(feature1, behaviour)
                .featureWithValue(feature2, value)
                .featureWithException(feature3, exceptionType)
                .featureWithDefaultException(feature4);
        final Set<FeatureMetadata> metadata =
                runner.configMetadata.get("DEFAULT_DIMMER_ENV");
        //then
        assertEquals(4, metadata.size());
        long count = metadata.stream()
                .filter(m-> feature1.equals(m.getFeature()))
                .map(m-> (FeatureMetadataBehaviour)m)
                .filter(m-> behaviour.equals(m.getBehaviour()))
                .count();
        assertEquals(1, count);

        count = metadata.stream()
                .filter(m-> feature2.equals(m.getFeature()))
                .map(m-> (FeatureMetadataValue)m)
                .filter(m-> value.equals(m.getValueToReturn()))
                .count();
        assertEquals(1, count);

        count = metadata.stream()
                .filter(m-> feature3.equals(m.getFeature()))
                .map(m-> (FeatureMetadataException)m)
                .filter(m-> exceptionType.equals(m.getException()))
                .count();
        assertEquals(1, count);


        count = metadata.stream()
                .filter(m-> feature4.equals(m.getFeature()))
                .map(m-> (FeatureMetadataDefaultException)m)
                .count();
        assertEquals(1, count);

    }

}