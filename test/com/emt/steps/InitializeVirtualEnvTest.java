package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

@RunWith(Theories.class)
public class InitializeVirtualEnvTest extends StepTestFixture {

    @Parameter(optional = true) boolean load_python;

    @StateVar boolean VenvExists;

    @DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
    @DataPoints("state") public static Map[] getState() { return _getState(); }

    private void commonSetup(Map args, Map state) {
        when(_steps.fileExists(anyString())).thenReturn((boolean)state.get("VenvExists"));
    }
    
    @Theory
    public void callsVirtualEnvWhenVenvDoesNotExist(@FromDataPoints("args") Map args,
                                                    @FromDataPoints("state") Map state) {
        assumeFalse((boolean)state.get("VenvExists"));
        
        commonSetup(args, state);
        
        inst().execute(args);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("virtualenv"));
    }

    @Theory
    public void doesNothingWhenVenvAlreadyExists(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        assumeTrue((boolean)state.get("VenvExists"));
        
        commonSetup(args, state);
        
        inst().execute(args);

        verify(_steps, never()).sh(anyString());
    }

    @Theory
    public void loadsPythonWhenRequested(@FromDataPoints("args") Map args,
                                         @FromDataPoints("state") Map state) {
        assumeFalse((boolean)state.get("VenvExists"));

        commonSetup(args, state);

        inst().execute(args);

        final boolean should_load_python = !args.containsKey("load_python") || (boolean) args.get("load_python");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("module load python") == should_load_python);
    }

}
