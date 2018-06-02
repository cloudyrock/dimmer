package net.cloudyrock.toggler;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.function.Function;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TogglerConfigurationUTest {

    private String feature;
    final Function<FeatureInvocation, Object> f1 = s -> "return1";
    final Function<FeatureInvocation, Object> f2 = s -> "return2";

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
    }

    @Test
    public void getHandler_return_added_value() {

        final Function<FeatureInvocation, Object> f1 = s -> "return1";
        TogglerConfiguration.toggleOffWithBehaviour(feature, f1);
        assertEquals(f1, TogglerConfiguration.getBehaviour(feature));
    }

    @Test
    public void toggleOffWithBehaviour() {

        boolean putFunction1 = TogglerConfiguration.toggleOffWithBehaviour(feature, f1);
        boolean putFunction2 = TogglerConfiguration.toggleOffWithBehaviour(feature, f2);
        boolean putDefaultException =
                TogglerConfiguration.toggleOffWithDefaultException(feature);
        boolean putValue = TogglerConfiguration.toggleOffWithValue(feature, "");
        boolean putException = TogglerConfiguration
                .toggleOffWithException(feature, DummyException.class);
        assertEquals(f1, TogglerConfiguration.getBehaviour(feature));
        assertTrue(putFunction1);
        assertFalse(putFunction2);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putException);

    }

    @Test
    public void toggleOffWithBehaviour_when_pass_null_feature_throws_exception() {

    }

    @Test
    public void toggleOffWithValue() {
        boolean putValue = TogglerConfiguration.toggleOffWithValue(feature, "VALUE");
        boolean putValue2 = TogglerConfiguration.toggleOffWithValue(feature, "VALUE");
        boolean putFunction = TogglerConfiguration.toggleOffWithBehaviour(feature, f1);
        boolean putDefaultException =
                TogglerConfiguration.toggleOffWithDefaultException(feature);
        boolean putException = TogglerConfiguration
                .toggleOffWithException(feature, DummyException.class);
        assertEquals("VALUE", TogglerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putValue);
        assertFalse(putValue2);
        assertFalse(putDefaultException);
        assertFalse(putFunction);
        assertFalse(putException);
    }

    @Test(expected = TogglerInvocationException.class)
    public void toggleOffWithDefaultException() {
        boolean putDefaultException =
                TogglerConfiguration.toggleOffWithDefaultException(feature);
        boolean putValue = TogglerConfiguration.toggleOffWithValue(feature, "VALUE");
        boolean putFunction = TogglerConfiguration.toggleOffWithBehaviour(feature, f1);
        boolean putException = TogglerConfiguration
                .toggleOffWithException(feature, DummyException.class);
        assertEquals("VALUE", TogglerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);
        assertFalse(putException);

        TogglerConfiguration.getBehaviour(feature).apply(null);
    }

    @Test(expected = DummyException.class)
    public void toggleOffWithException() {
        boolean putException = TogglerConfiguration
                .toggleOffWithException(feature, DummyException.class);
        boolean putDefaultException =
                TogglerConfiguration.toggleOffWithDefaultException(feature);
        boolean putValue = TogglerConfiguration.toggleOffWithValue(feature, "VALUE");
        boolean putFunction = TogglerConfiguration.toggleOffWithBehaviour(feature, f1);
        assertEquals("VALUE", TogglerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putException);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);

        TogglerConfiguration.getBehaviour(feature).apply(null);
    }

    @Test(expected = TogglerConfigurationException.class)
    public void toggleWithException_throws_exception_when_constructor_not_accessible() {
        TogglerConfiguration
                .toggleOffWithException(feature, DummyNoConstructorException.class);
        TogglerConfiguration.getBehaviour(feature).apply(null);
    }

}
