package com.github.cloudyrock.dimmer;

class BehaviourKey {

    private final String feature;
    private final String operation;

    BehaviourKey(String feature, String operation) {
        checkProperty(feature, "Feature");
        checkProperty(operation, "Operation");
        this.feature = feature;
        this.operation = operation;
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

        final BehaviourKey that = (BehaviourKey) o;

        return feature.equals(that.feature) && operation.equals(that.operation);
    }

    @Override
    public int hashCode() {
        return 31 * feature.hashCode() + operation.hashCode();
    }



    private void checkProperty(String feature, String propertyName1) {
        if(feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException(propertyName1 + " cannot be empty");
        }
    }
}
