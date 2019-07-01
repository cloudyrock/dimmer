package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.FeatureInvocation;

import java.util.function.Function;

public final class CustomExceptionFeatureMetadata extends FeatureMetadata {

    private final Class<? extends RuntimeException> exceptionType;

	public CustomExceptionFeatureMetadata(String feature,
                                          String operation,
                                          Class<? extends RuntimeException> exceptionType) {
        super(feature, operation);
        this.exceptionType = exceptionType;
    }

    public Class<? extends RuntimeException> getException() {
        return exceptionType;
    }


	public Function<FeatureInvocation, Object> getFunction() {
		return featureInv -> ExceptionUtil.throwException(getException(), featureInv);
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
