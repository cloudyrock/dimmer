package com.github.cloudyrock.dimmer.spring;


import com.github.cloudyrock.dimmer.ConfigRepository;
import com.github.cloudyrock.dimmer.ConfigService;
import com.github.cloudyrock.dimmer.FileConfigRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DimmerSpringBootAppUTest: Unit tests")
class DimmerSpringBootAppUTest {


    private final DimmerSpringBootApp app = new DimmerSpringBootApp();

    @Test
    @DisplayName("Should return FileConfigRepositoryImpl when configRepository")
    void shouldReturnFileConfigRepositoryImplWhenConfigRepository() throws NoSuchFieldException, IllegalAccessException {
        final FeatureConfig featureConfig = new FeatureConfig();
        featureConfig.setEnvironments(new HashMap<>());
        checkField(app.configRepository(featureConfig), "configByEnv", featureConfig.getEnvironments());
    }

    @Test
    @DisplayName("Should return ConfigService when configService")
    void shouldReturnConfigServiceWhenConfigService() throws NoSuchFieldException, IllegalAccessException {
        final ConfigRepository repo = new FileConfigRepositoryImpl(new HashMap<>());
        checkField(app.configService(repo), "repository", repo);
    }

    @Test
    @DisplayName("Should return ConfigController when configController")
    void shouldReturnConfigControllerWhenConfigController() throws NoSuchFieldException, IllegalAccessException {
        final ConfigService service = new ConfigService(new FileConfigRepositoryImpl(new HashMap<>()));
        checkField(app.configController(service), "configService", service);
    }

    private void checkField(Object object, String fieldName, Object expected) throws NoSuchFieldException, IllegalAccessException {
        final Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        assertEquals(expected, field.get(object));
    }

}