package com.emt.steps;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class SayHelloTest extends StepTestFixture {

    @Test
    public void callsEchoWithDefaultMessage() {
    	inst().execute();
        verify(_steps).echo("Hello, human.");
    }
    
    @Test
    public void callsEchoWithCustomMessage() {
    	inst().execute(ImmutableMap.of("name", "World"));
        verify(_steps).echo("Hello, World.");
    }

}
