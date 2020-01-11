package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class CreatePuppetDockerfileTest extends StepTestFixture {

    @Test
    public void callsShWithCommand() {
    	_steps.env.put("WORKSPACE", "/dummy/path/to/workspace");
        inst().execute();
        verify(_steps).sh(anyString());
    }

}
