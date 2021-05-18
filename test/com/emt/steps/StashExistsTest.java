package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;
import com.emt.util.StateVar;

import hudson.AbortException;


public class StashExistsTest extends StepTestFixture {

	@Parameter(values = {"my_stash_name"}) String name;

	@StateVar boolean StashExists;
	
	private void commonSetup(Map args, Map state) {
		if ((boolean)state.get("StashExists") == false) {
			try {
				doThrow(hudson.AbortException.class).when(_steps).unstash(any(Map.class));
			} catch (AbortException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Theory
    public void returnsClosureReturnValue(@FromDataPoints("args") Map args,
    									  @FromDataPoints("state") Map state) {
		commonSetup(args, state);
    	assert execute(args).equals(state.get("StashExists"));
    }

}
