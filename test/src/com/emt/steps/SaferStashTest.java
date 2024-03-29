package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;
import com.emt.util.StateVar;


public class SaferStashTest extends StepTestFixture {

	@Parameter(values="my_stash_name") String name;
	@Parameter(values="my_includes") String includes;
	
	@StateVar boolean StashExists;
	
	protected void commonSetup(Map args, Map state) {
		when(_steps.stashExists(anyString())).thenReturn((boolean)state.get("StashExists"));
	}

	@Theory
    public void callsStashWhenStashDoesNotExist(@FromDataPoints("args") Map args,
    					   					    @FromDataPoints("state") Map state) {
		assumeFalse((boolean)state.get("StashExists"));
		commonSetup(args, state);
    	execute(args);
    	verify(_steps, times(1)).stash(any(Map.class));
    }
	
	@Theory
    public void failsWhenStashExists(@FromDataPoints("args") Map args,
    								 @FromDataPoints("state") Map state) {
		assumeTrue((boolean)state.get("StashExists"));
		commonSetup(args, state);
    	execute(args);
    	assertTrue(error_was_called());
    }

}
