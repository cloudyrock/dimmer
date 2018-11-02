package com.github.cloudyrock.dimmer;

import java.util.Set;

public interface ConfigRepository {

    Set<String> findFeaturesInterceptedByEnvironment(String environment);

}
