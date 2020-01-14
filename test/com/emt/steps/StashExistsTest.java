package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import hudson.AbortException;

@RunWith(Theories.class)
public class StashExistsTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("name");
	}

	public static List<String> getStateVariables() {
		return Arrays.asList("StashExists");
	}
	
    public static Object[] name_values() {
    	return new Object[] { "my_stash_name" };
    }

    public static Object[] StashExists_values() {
    	return new Object[]{ true, false };
    }

	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
	@DataPoints("state") public static Map[] getState() { return _getState(); }
	
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
