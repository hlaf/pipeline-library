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


public class IsBuildReplayedTest extends StepTestFixture {

    private static final String replay_cause = "org.jenkinsci.plugins.workflow.cps.replay.ReplayCause";

    public static Object[] build_cause_values() {
        return new Object[] { Arrays.asList(replay_cause), Arrays.asList() };
    }

    @StateVar List<String> build_cause;

    public void setup() {
        super.setup();
    }

    @Theory
    public void returnsCorrectValue(@FromDataPoints("state") Map state) {
        List<String> build_cause = (List<String>) state.get("build_cause");
        final boolean expected_res = build_cause.contains(replay_cause);
        JSONArray a = new JSONArray();
        for (String cause: build_cause) a.add(cause);
        try {
            when(_steps.currentBuild.getBuildCauses(replay_cause)).thenReturn(a);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(execute(new HashMap<>()).equals(expected_res));
    }

}
