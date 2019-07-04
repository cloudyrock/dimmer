package com.github.cloudyrock.dimmer;

import java.util.Objects;

class ArgumentClass {


    private final String value;

    ArgumentClass(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentClass that = (ArgumentClass) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
