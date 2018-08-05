package com.github.cloudyrock.dimmer.api;

import com.github.cloudyrock.dimmer.DimmerFeatureService;

public class DimmerFeatureServiceApi {

    private final DimmerFeatureService dimmerFeatureService;
    private final FeatureInvocationResponseMapper featureInvocationResponseMapper;

    DimmerFeatureServiceApi(DimmerFeatureService service, FeatureInvocationResponseMapper mapper) {
        this.dimmerFeatureService = service;
        this.featureInvocationResponseMapper = mapper;
    }

    //POST
    public FeatureInvocationResponse executeFeature(FeatureInvocationRequest featureInvocationRequest) {

        Object object = dimmerFeatureService
                .invokeFeatureForEnvironment(
                        featureInvocationRequest.getEnvironment(),
                        featureInvocationRequest.getFeatureName(),
                        featureInvocationRequest.getFeatureInvocation());
        return featureInvocationResponseMapper.mapResponse(object);
    }

}
