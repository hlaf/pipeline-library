package com.emt.steps;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.ICurrentBuildNamespace;

@RunWith(Theories.class)
public class IsBuildStartedByTimerTest extends StepTestFixture {

	private static final String timer_cause = "hudson.triggers.TimerTrigger.TimerTriggerCause";

	@DataPoints("build_cause")
    public static List<List<String>> build_cause_values() {
		List<List<String>> res = new ArrayList<List<String>>();
		res.add(Arrays.asList("dummy_cause", timer_cause));
		res.add(Arrays.asList(timer_cause));
		res.add(Arrays.asList("dummy_cause1", "dummy_cause2"));
		return res;
    }

    public IsBuildStartedByTimer inst() {
    	return new IsBuildStartedByTimer(_steps);
    }
    
    public void setup() {
    	super.setup();
    	_steps.currentBuild = mock(ICurrentBuildNamespace.class);
    }

    @Theory
    public void returnsTrue(@FromDataPoints("build_cause") List<String> build_causes) {
    	assumeTrue(build_causes.contains(timer_cause));
        when(_steps.currentBuild.getBuildCauses()).thenReturn(build_causes);
    	assertTrue(inst().execute());
    }
    
    @Theory
    public void returnsFalse(@FromDataPoints("build_cause") List<String> build_causes) {
    	assumeFalse(build_causes.contains(timer_cause));
        when(_steps.currentBuild.getBuildCauses()).thenReturn(build_causes);
    	assertFalse(inst().execute());
    }
}
 