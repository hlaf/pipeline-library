package com.emt.steps;

import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SayHelloTest extends StepTestFixture {
    
    public SayHello inst() {
    	return new SayHello(_steps);
    }
    
    @Test
    public void callsEchoWithDefaultMessage() {
    	inst().execute();
        verify(_steps).echo("Hello, human.");
    }
    
    @Test
    public void callsEchoWithCustomMessage() {
    	inst().execute("World");
        verify(_steps).echo("Hello, World.");
    }

}
