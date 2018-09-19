package com.github.cloudyrock.dimmer;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class FeatureInvocationUTest {

    @Test
    public void constructor() {
        final FeatureInvocation fi = new FeatureInvocation(
                "feature",
                "operation",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg1"},
                String.class
        );
        assertEquals("feature", fi.getFeature());
        assertEquals("operation", fi.getOperation());
        assertEquals("methodName", fi.getMethodName());
        assertEquals(FeatureInvocationUTest.class, fi.getDeclaringType());
        assertEquals("arg1", fi.getArgs()[0]);
        assertEquals(1, fi.getArgs().length);
        assertEquals(String.class, fi.getReturnType());
    }


    @Test
    public void equalsAndHashCode() {
        final FeatureInvocation fiEqual1 = new FeatureInvocation(
                "feature",
                "operation",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg1"},
                String.class
        );
        final FeatureInvocation fiEqual2 = new FeatureInvocation(
                "feature",
                "operation",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg3", "whatever"},
                String.class
        );

        final FeatureInvocation fiDifferent1 = new FeatureInvocation(
                "featureDifferent",
                "operation",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg3", "whatever"},
                String.class
        );

        final FeatureInvocation fiDifferent2 = new FeatureInvocation(
                "feature",
                "operationDifferent",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg3", "whatever"},
                String.class
        );

        final FeatureInvocation fiDifferent3 = new FeatureInvocation(
                "feature",
                "operation",
                "methodNameDifferent",
                FeatureInvocationUTest.class,
                new Object[]{"arg3", "whatever"},
                String.class
        );

        final FeatureInvocation fiDifferent4 = new FeatureInvocation(
                "feature",
                "operation",
                "methodName",
                String.class,
                new Object[]{"arg3", "whatever"},
                String.class
        );
        final FeatureInvocation fiDifferent5 = new FeatureInvocation(
                "feature",
                "operation",
                "methodName",
                FeatureInvocationUTest.class,
                new Object[]{"arg1"},
                FeatureInvocationUTest.class
        );
        assertTrue(fiEqual1.equals(fiEqual1));
        assertFalse(fiEqual1.equals(null));
        assertFalse(fiEqual1.equals(""));
        assertEquals(fiEqual2, fiEqual1);
        assertNotEquals(fiEqual1, fiDifferent1);
        assertNotEquals(fiEqual1, fiDifferent2);
        assertNotEquals(fiEqual1, fiDifferent3);
        assertNotEquals(fiEqual1, fiDifferent4);
        assertNotEquals(fiEqual1, fiDifferent5);

        final Set<FeatureInvocation> fiSet = new HashSet();
        fiSet.add(fiEqual1);
        fiSet.add(fiEqual1);
        fiSet.add(fiDifferent1);
        fiSet.add(fiDifferent2);
        fiSet.add(fiDifferent3);
        fiSet.add(fiDifferent4);
        fiSet.add(fiDifferent5);
        assertEquals(6, fiSet.size());
        assertTrue(fiSet.contains(fiEqual1));
        assertTrue(fiSet.contains(fiEqual2));
        assertTrue(fiSet.contains(fiDifferent1));
        assertTrue(fiSet.contains(fiDifferent2));
        assertTrue(fiSet.contains(fiDifferent3));
        assertTrue(fiSet.contains(fiDifferent4));
        assertTrue(fiSet.contains(fiDifferent5));
    }

}