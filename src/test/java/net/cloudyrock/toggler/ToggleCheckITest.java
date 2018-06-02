package net.cloudyrock.toggler;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ToggleCheckITest {


    private static final String REAL_VALUE = "real_value";
    private static final String FEATURE1 = "FEATURE1";
    private static final String FEATURE2 = "FEATURE2";
    private static final String FEATURE3 = "FEATURE3";
    private static final String FEATURE4 = "FEATURE4";
    private static final String FEATURE5 = "FEATURE5";

    @Test
    public void toggleOffWithBehaviour_return_mock_value() {
        String mock_value3 = "MOCK_VALUE3";
        assertTrue(TogglerConfiguration.toggleOffWithBehaviour(
                FEATURE1,
                s -> mock_value3));
        assertEquals(mock_value3, new DummyToggleCheckClass().feature1Method());
    }

    @Test
    public void toggleOffWithValue_return_mock_value() {
        String mock_value1 = "MOCK_VALUE1";
        assertTrue(TogglerConfiguration.toggleOffWithValue(FEATURE3, mock_value1));
        assertEquals(mock_value1, new DummyToggleCheckClass().feature3Method());
    }

    @Test(expected = TogglerInvocationException.class)
    public void toggleOffWithException_throws_exception() {
        assertTrue(TogglerConfiguration.toggleOffWithDefaultException(FEATURE2));
        new DummyToggleCheckClass().feature2Method();
    }



    @Test
    public void toggleOffWithValue_return_null_when_arg_is_null() {
        assertTrue(TogglerConfiguration.toggleOffWithValue(FEATURE5, null));
        assertEquals(null, new DummyToggleCheckClass().feature5Method());
    }

    @Test
    public void real_method_is_called_when_feature_not_configured() {
        assertEquals(REAL_VALUE, new DummyToggleCheckClass().feature4Method());
    }

    class DummyToggleCheckClass {

        @ToggleCheck(feature = FEATURE1)
        String feature1Method() {
            return REAL_VALUE;
        }

        @ToggleCheck(feature = FEATURE2)
        void feature2Method() {
        }

        @ToggleCheck(feature = FEATURE3)
        String feature3Method() {
            return REAL_VALUE;
        }

        @ToggleCheck(feature = FEATURE4)
        String feature4Method() {
            return REAL_VALUE;
        }

        @ToggleCheck(feature = FEATURE5)
        String feature5Method() {
            return REAL_VALUE;
        }
    }

}
