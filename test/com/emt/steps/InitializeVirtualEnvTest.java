package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

@RunWith(Theories.class)
public class InitializeVirtualEnvTest extends StepTestFixture {
	
	@Parameter(optional=true) boolean load_python;

	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
	
	@Theory
    public void callsVirtualEnv(@FromDataPoints("args") Map args) {
    	inst().execute(args);
    	    	
    	ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("virtualenv"));
    }

	@Theory
    public void loadsPythonWhenRequested(@FromDataPoints("args") Map args) {
    	inst().execute(args);
    	
    	final boolean should_load_python = !args.containsKey("load_python") || (boolean) args.get("load_python");
    	
    	ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("module load python") == should_load_python);
    }
	
}
