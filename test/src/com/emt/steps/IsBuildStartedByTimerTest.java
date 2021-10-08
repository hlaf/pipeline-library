package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.mockito.Mockito;

import com.emt.ICurrentBuildNamespace;
import com.emt.util.StateVar;


public class IsBuildStartedByTimerTest extends StepTestFixture {

	private static final String timer_cause = "hudson.triggers.TimerTrigger$TimerTriggerCause";
	
	@StateVar List<String> build_cause;

    public static Object[] build_cause_values() {
		return new Object[] { Arrays.asList(timer_cause), Arrays.asList() };
    }

    public void setup() {
    	super.setup();
    	_steps.currentBuild = mock(ICurrentBuildNamespace.class, Mockito.CALLS_REAL_METHODS);
    }

    @Theory
    public void predicateWorks(@FromDataPoints("state") Map state) {
        List<String> build_cause = (List<String>) state.get("build_cause");
    	final boolean expected_res = build_cause.contains(timer_cause);
        when(_steps.currentBuild.getBuildCauses(timer_cause)).thenReturn(build_cause);
    	assertTrue(execute(new HashMap()).equals(expected_res));
    }

}
 