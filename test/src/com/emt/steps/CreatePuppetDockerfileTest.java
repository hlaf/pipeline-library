package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class CreatePuppetDockerfileTest extends StepTestFixture {

    @Parameter String image_name;
    @Parameter(optional=true) String image_user;
    @Parameter(optional=true) String environment;
    @Parameter(optional=true) String master;
    @Parameter(optional=true) String from_image_name;

    @Theory
    public void callsShWithCommand(@FromDataPoints("args") Map args) {
    	_steps.env.put("WORKSPACE", "/dummy/path/to/workspace");
        execute(args);
        verify(_steps).sh(anyString());
    }

}
