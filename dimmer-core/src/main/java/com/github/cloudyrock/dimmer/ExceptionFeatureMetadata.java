package com.github.cloudyrock.dimmer;

public final class ExceptionFeatureMetadata extends FeatureMetadata {

    private final Class<? extends RuntimeException> exceptionType;

    ExceptionFeatureMetadata(String feature,
							 String operation,
							 Class<? extends RuntimeException> exceptionType) {
        super(feature, operation);
        this.exceptionType = exceptionType;
    }

    public Class<? extends RuntimeException> getException() {
        return exceptionType;
    }


	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
