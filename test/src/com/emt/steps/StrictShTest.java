package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class StrictShTest extends StepTestFixture {

	@Parameter(values = {"ls"}) String script;

	@Theory
    public void callsShWithStrictOptions(@FromDataPoints("args") Map args,
    									 @FromDataPoints("state") Map state) {
    	execute(args);

        verify(_steps).sh(_captor_map.capture());
        String sh_script = _captor_map.getValue().get("script").toString();
        assertTrue(sh_script.contains("set -o pipefail"));
        assertTrue(sh_script.contains("set -e"));
        assertTrue(sh_script.contains((String)args.get("script")));
    }

}
