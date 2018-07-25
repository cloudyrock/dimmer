package com.github.cloudyrock.dimmer;

public class AnnotatedClass {
    static final String REAL_VALUE = "real_value";
    static final String FEATURE1 = "FEATURE1";
    static final String FEATURE2 = "FEATURE2";
    static final String FEATURE3 = "FEATURE3";
    static final String FEATURE4 = "FEATURE4";
    static final String FEATURE5 = "FEATURE5";
    static final String FEATURE6 = "FEATURE6";
    static final String FEATURE7 = "FEATURE7";

    static final String VALUE1 = "VALUE1";
    static final String FEATURE8 = "FEATURE8";
    static final String FEATURE9 = "FEATURE9";
    static final String FEATURE10 = "FEATURE10";
    
    @DimmerFeature(value = FEATURE1)
    String methodForFeature1() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE2)
    String methodForFeature2(String param1, Integer param2) {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE3)
    String methodForFeature3() {
        return "ERROR";
    }

    @DimmerFeature(value = FEATURE4)
    String methodForFeature4() {
        return "REAL VALUE";
    }

    @DimmerFeature(value = FEATURE5)
    void methodForFeature5() {
    }

    @DimmerFeature(value = FEATURE6)
    String methodForFeature6(String param1, Integer param2) {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE7)
    String methodForFeature7() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE8)
    String methodForFeature8() {
        return null;
    }

    @DimmerFeature(value = FEATURE9)
    Parent methodForFeature9() {
        return new Parent();
    }

    @DimmerFeature(value = FEATURE10)
    public void methodForFeature10() {
    }
}
