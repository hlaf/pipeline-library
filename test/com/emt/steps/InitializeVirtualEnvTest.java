package com.emt.steps;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;

import org.junit.Test;

public class InitializeVirtualEnvTest extends StepTestFixture {

	@Test
    public void callsSh() {
    	inst().execute();
        verify(_steps).sh(anyString());
    }

}
