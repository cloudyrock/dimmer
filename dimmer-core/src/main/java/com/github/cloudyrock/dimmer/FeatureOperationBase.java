package com.github.cloudyrock.dimmer;

public class FeatureOperationBase {

	protected final String feature;
	protected final String operation;

	FeatureOperationBase(String feature, String operation) {
		checkProperty(feature, "Feature");
		this.feature = feature;
		this.operation = operation != null ? operation : "";
	}

	protected void checkProperty(String feature, String propertyName1) {
		if(feature == null || feature.isEmpty()) {
			throw new IllegalArgumentException(propertyName1 + " cannot be empty");
		}
	}

	public String getFeature() {
		return feature;
	}

	public String getOperation() {
		return operation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final FeatureOperationBase that = (FeatureOperationBase) o;

		return feature.equals(that.feature) && operation.equals(that.operation);
	}

	@Override
	public int hashCode() {
		return 31 * feature.hashCode() + operation.hashCode();
	}
}
