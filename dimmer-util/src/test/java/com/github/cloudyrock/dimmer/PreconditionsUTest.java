package com.github.cloudyrock.dimmer;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;


public class PreconditionsUTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenIsNullObject(){
        Preconditions.checkNullOrEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenIsEmptyString(){
        Preconditions.checkNullOrEmpty("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenIsEmptyArray(){
        Preconditions.checkNullOrEmpty(Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenIsEmptyMap(){
        Preconditions.checkNullOrEmpty(new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenOptionalIsNotPresent(){
        Preconditions.checkNullOrEmpty(Optional.empty());
    }

    @Test
    public void objectIsNull(){
        Preconditions.isNullOrEmpty(null);
    }

    @Test
    public void stringIsEmpty(){
        assertThat(Preconditions.isNullOrEmpty(""), is(true));
    }

}