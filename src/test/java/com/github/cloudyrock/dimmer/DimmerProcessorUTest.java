package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;
import com.github.cloudyrock.dimmer.exceptions.DummyException;
import com.github.cloudyrock.dimmer.exceptions.DummyNoConstructorException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;
import java.util.function.Function;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DimmerProcessorUTest extends DimmerTestBase {

    private String feature;
    final Function<FeatureInvocation, Object> f1 = s -> "return1";
    final Function<FeatureInvocation, Object> f2 = s -> "return2";

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
    }

    @Test
    public void executeIfExistsOrSupplier_executes_supplier_when_missing_feature() throws Throwable {

        // given
        ProceedingJoinPoint realMethodMock = mock(ProceedingJoinPoint.class);
        given(realMethodMock.proceed()).willReturn("VALUE");

        // when
        Object result = dimmerProcessor.runBehaviourIfExistsOrReaInvocation(
                feature,
                null,
                realMethodMock);
        // then
        assertEquals("VALUE", result);
    }

    @Test
    public void executeIfExistsOrRealMethod_executes_feature_when_is_there() throws Throwable {

        // given
        final Function<FeatureInvocation, String> behaviour = s -> "BEHAVIOUR VALUE";

        dimmerProcessor.featureWithBehaviour(feature, behaviour);
        Object result = dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null);
        assertEquals("BEHAVIOUR VALUE", result);
    }

    @Test
    public void behaviour_is_executed_with_right_parameter() throws Throwable {
        // given
        Function<FeatureInvocation, String> behaviourMock = mock(Function.class);
        dimmerProcessor.featureWithBehaviour(feature, behaviourMock);
        FeatureInvocation f = new FeatureInvocation(null, null, null);

        // when
        dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, f, null);

        // then
        final ArgumentCaptor<FeatureInvocation> captor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        verify(behaviourMock).apply(captor.capture());
        assertTrue(captor.getValue() == f);
    }

    @Test
    public void getBehaviour_return_added_value() throws Throwable {

        final Function<FeatureInvocation, Object> f1 = s -> "return1";
        dimmerProcessor.featureWithBehaviour(feature, f1);
        assertEquals("return1", dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null));
    }

    @Test
    public void featureOffWithBehaviour() throws Throwable {

        boolean putFunction1 = dimmerProcessor.featureWithBehaviour(feature, f1);
        boolean putFunction2 = dimmerProcessor.featureWithBehaviour(feature, f2);
        boolean putDefaultException =
                dimmerProcessor.featureWithDefaultException(feature);
        boolean putValue = dimmerProcessor.featureWithValue(feature, "");
        boolean putException = dimmerProcessor
                .featureWithException(feature, DummyException.class);
        assertEquals(f1.apply(null), dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null));
        assertTrue(putFunction1);
        assertFalse(putFunction2);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putException);

    }

    @Test
    public void featureOffWithValue() throws Throwable {
        boolean putValue = dimmerProcessor.featureWithValue(feature, "VALUE");
        boolean putValue2 = dimmerProcessor.featureWithValue(feature, "VALUE");
        boolean putFunction = dimmerProcessor.featureWithBehaviour(feature, f1);
        boolean putDefaultException =
                dimmerProcessor.featureWithDefaultException(feature);
        boolean putException = dimmerProcessor
                .featureWithException(feature, DummyException.class);
        assertEquals("VALUE", dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null));
        assertTrue(putValue);
        assertFalse(putValue2);
        assertFalse(putDefaultException);
        assertFalse(putFunction);
        assertFalse(putException);
    }

    @Test(expected = DefaultException.class)
    public void featureOffWithDefaultException() throws Throwable {
        boolean putDefaultException =
                dimmerProcessor.featureWithDefaultException(feature);
        boolean putValue = dimmerProcessor.featureWithValue(feature, "VALUE");
        boolean putFunction = dimmerProcessor.featureWithBehaviour(feature, f1);
        boolean putException = dimmerProcessor
                .featureWithException(feature, DummyException.class);
        assertEquals("VALUE", dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null));
        assertTrue(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);
        assertFalse(putException);
    }

    @Test(expected = DummyException.class)
    public void featureOffWithException() throws Throwable {
        boolean putException = dimmerProcessor
                .featureWithException(feature, DummyException.class);
        boolean putDefaultException =
                dimmerProcessor.featureWithDefaultException(feature);
        boolean putValue = dimmerProcessor.featureWithValue(feature, "VALUE");
        boolean putFunction = dimmerProcessor.featureWithBehaviour(feature, f1);
        assertEquals("VALUE", dimmerProcessor
                .runBehaviourIfExistsOrReaInvocation(feature, null, null));
        assertTrue(putException);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);
    }

    @Test(expected = DimmerConfigException.class)
    public void featureWithException_throws_exception_when_constructor_not_accessible() throws Throwable {
        dimmerProcessor
                .featureWithException(feature, DummyNoConstructorException.class);
        dimmerProcessor.runBehaviourIfExistsOrReaInvocation(feature, null, null);
    }

    @Test
    public void throws_custom_default_exception() {

    }

}
