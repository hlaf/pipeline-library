package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.emt.util.StateVar;


public class InitializeVirtualEnvTest extends StepTestFixture {

    @StateVar boolean VenvExists;
    @StateVar boolean ModulesCommandAvailable;

    private void commonSetup(Map args, Map state) {
        when(_steps.fileExists(anyString())).thenReturn((boolean)state.get("VenvExists"));
        when(_steps.sh(any(Map.class))).thenReturn((boolean)state.get("ModulesCommandAvailable") ? 0:1);
    }
    
    @Theory
    public void callsVirtualEnvWhenVenvDoesNotExist(@FromDataPoints("args") Map args,
                                                    @FromDataPoints("state") Map state) {
        assumeFalse((boolean)state.get("VenvExists"));
        
        commonSetup(args, state);
        
        execute(args);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("virtualenv"));
    }

    @Theory
    public void doesNothingWhenVenvAlreadyExists(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        assumeTrue((boolean)state.get("VenvExists"));

        commonSetup(args, state);

        execute(args);

        verify(_steps, never()).sh(anyString());
    }

    @Theory
    public void usesModulesCommandWhenAvailable(@FromDataPoints("args") Map args,
                                                @FromDataPoints("state") Map state) {
        assumeFalse((boolean)state.get("VenvExists"));
        
        commonSetup(args, state);
        
        execute(args);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("module load") == (boolean)state.get("ModulesCommandAvailable"));
    }

}
