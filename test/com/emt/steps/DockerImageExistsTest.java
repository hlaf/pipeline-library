package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Test;

public class DockerImageExistsTest extends StepTestFixture {

    @Test
    public void callsShWithCommand() {
        inst().execute();
        verify(_steps).sh(any(Map.class));
    }

}
