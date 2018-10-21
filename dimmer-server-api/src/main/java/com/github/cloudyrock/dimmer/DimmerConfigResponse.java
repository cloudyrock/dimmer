package com.github.cloudyrock.dimmer;

public final class DimmerConfigResponse {

    private String value;

    public DimmerConfigResponse() {

    }

    public DimmerConfigResponse(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DimmerConfigResponse{" +
                "value='" + value + '\'' +
                '}';
    }
}
