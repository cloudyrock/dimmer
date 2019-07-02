package com.github.cloudyrock.dimmer.behaviour;

public class BehaviourKey extends FeatureOperationBase{


    public BehaviourKey(String feature, String operation) {
        super(feature, operation);
        checkProperty(operation, "Operation");
    }

}
