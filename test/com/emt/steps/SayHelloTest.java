package com.emt.steps;

import org.somecompany.IStepExecutor;
import org.somecompany.ioc.ContextRegistry;
import org.somecompany.ioc.IContext;
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

    @Test
    public void build_callsShStep() {
//        // prepare
//        String solutionPath = "some/path/to.sln";
//        MsBuild builder = new MsBuild(solutionPath);
          new SayHello().execute();
    	
    	
//        // execute
//        builder.build();
//
//        // verify
//        verify(_steps).sh(anyString());
    }

}
