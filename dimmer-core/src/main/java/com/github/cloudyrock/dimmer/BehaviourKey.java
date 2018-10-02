package com.github.cloudyrock.dimmer;

class BehaviourKey extends FeatureOperationBase{


    BehaviourKey(String feature, String operation) {
        super(feature, operation);
        checkProperty(operation, "Operation");
    }

}
