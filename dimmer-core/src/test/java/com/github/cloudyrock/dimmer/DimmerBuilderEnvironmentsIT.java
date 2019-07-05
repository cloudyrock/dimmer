package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DimmerBuilderEnvironmentsIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //Environments
    @Test
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowConfigurationExceptionWhenRunningNonExistingEnvironment() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage(new Contains("The selected environment doesn't exist in the Dimmer configuration file"));
        expectedException.expectMessage(new Contains("WRONG-ENV"));
        BuilderTestUtil.setUp(DimmerInvocationException.class, "WRONG-ENV");
    }


}
