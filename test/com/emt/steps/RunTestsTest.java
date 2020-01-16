package com.emt.steps;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class RunTestsTest extends StepTestFixture {

	@Parameter(values={"my_tox_environment"}, optional=true) String environment;
	@Parameter(values={"my_label"}, optional=true) String label;
	
	@StateVar boolean IsUnix;
	@StateVar boolean ComputeCoverage;
	
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
	@DataPoints("state") public static Map[] getState() { return _getState(); }
	
	private void commonSetup(Map args, Map state) {
		when(_steps.isUnix()).thenReturn((boolean)state.get("IsUnix"));
		Map config = new HashMap<>();
		config.put("compute_coverage", (boolean) state.get("ComputeCoverage"));
		when(_steps.getPipelineConfig()).thenReturn(config);
	}

	@Theory
    public void testsAreRun(@FromDataPoints("args") Map args,
    		        		@FromDataPoints("state") Map state) {
    	commonSetup(args, state);
    	inst().execute(args);
    	verify(_steps).publishJUnitReport();
    }

}
