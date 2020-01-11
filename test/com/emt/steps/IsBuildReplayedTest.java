package com.emt.steps;

import static org.junit.Assert.assertTrue;
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
public class IsBuildReplayedTest extends StepTestFixture {

	private static final String replay_cause = "org.jenkinsci.plugins.workflow.cps.replay.ReplayCause";

	@DataPoints("build_cause")
    public static List<List<String>> build_cause_values() {
		List<List<String>> res = new ArrayList<List<String>>();
		res.add(Arrays.asList(replay_cause));
		res.add(Arrays.asList());
		return res;
    }

    public void setup() {
    	super.setup();
    	_steps.currentBuild = mock(ICurrentBuildNamespace.class);
    }

    @Theory
    public void returnsCorrectValue(@FromDataPoints("build_cause") List<String> build_causes) {
        final boolean expected_res = build_causes.contains(replay_cause);
        when(_steps.currentBuild.getBuildCauses(replay_cause)).thenReturn(build_causes);
    	assertTrue(inst().execute().equals(expected_res));
    }

}
 