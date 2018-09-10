package com.github.cloudyrock.dimmer;

public final class FeatureMetadataException extends FeatureMetadata {

    private final Class<? extends RuntimeException> exceptionType;

    FeatureMetadataException(String feature,
                             String operation,
                             Class<? extends RuntimeException> exceptionType) {
        super(feature, operation);
        this.exceptionType = exceptionType;
    }

    public Class<? extends RuntimeException> getException() {
        return exceptionType;
    }
}
