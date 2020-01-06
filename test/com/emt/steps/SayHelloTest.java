package com.emt.steps;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.emt.IStepExecutor;
import com.emt.ioc.ContextRegistry;
import com.emt.ioc.IContext;

public class SayHelloTest extends StepTestFixture {
    private IContext _context;
    private IStepExecutor _steps;

    @Before
    public void setup() {
        _context = mock(IContext.class);
        _steps = mock(IStepExecutor.class);

        when(_context.getStepExecutor()).thenReturn(_steps);

        ContextRegistry.registerContext(_context);
    }

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
