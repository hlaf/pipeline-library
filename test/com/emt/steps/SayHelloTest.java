package com.emt.steps;

import com.emt.IStepExecutor;
import com.emt.ioc.ContextRegistry;
import com.emt.ioc.IContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SayHelloTest {
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
