package com.github.cloudyrock.dimmer.api;

public class FeatureInvocationResult {

    private Object result;
    private DimmerStatus dimmerStatus;


    public Object getResult() {
        return result;
    }


    public void setResult(Object result) {
        this.result = result;
    }


    public DimmerStatus getDimmerStatus() {
        return dimmerStatus;
    }

    public void setDimmerStatus(DimmerStatus dimmerStatus) {
        this.dimmerStatus = dimmerStatus;
    }
}
