package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.mockito.Mockito;

import com.emt.util.StateVar;

import net.sf.json.JSONArray;


public class IsBuildStartedByTimerTest extends StepTestFixture {

	private static final String timer_cause = "hudson.triggers.TimerTrigger$TimerTriggerCause";
	
	@StateVar List<String> build_cause;

    public static Object[] build_cause_values() {
		return new Object[] { Arrays.asList(timer_cause), Arrays.asList() };
    }

    @Theory
    public void predicateWorks(@FromDataPoints("state") Map state) {
        List<String> build_cause = (List<String>) state.get("build_cause");
    	final boolean expected_res = build_cause.contains(timer_cause);
    	JSONArray a = new JSONArray();
        for (String cause: build_cause) a.add(cause);
        try {
            when(_steps.currentBuild.getBuildCauses(timer_cause)).thenReturn(a);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    	assertTrue(execute(new HashMap()).equals(expected_res));
    }

}
 