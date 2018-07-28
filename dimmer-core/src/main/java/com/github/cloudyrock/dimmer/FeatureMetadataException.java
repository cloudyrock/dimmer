package com.github.cloudyrock.dimmer;

public final class FeatureMetadataException extends FeatureMetadata {

    private final Class<? extends RuntimeException> exceptionType;

    public FeatureMetadataException(String feature,
                                    Class<? extends RuntimeException> exceptionType) {
        super(feature);
        this.exceptionType = exceptionType;
    }

    public Class<? extends RuntimeException> getException() {
        return exceptionType;
    }
}
