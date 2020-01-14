package com.emt.steps;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class RunTestsTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("environment", "label");
	}

	public static List<String> getStateVariables() {
		return Arrays.asList("IsUnix", "ComputeCoverage");
	}

    public static Object[] environment_values() {
        return new Object[]{ "my_tox_environment", new Unassigned() };
    }
	
    public static Object[] label_values() {
        return new Object[]{ "my_label", new Unassigned() };
    }
    
    public static Object[] IsUnix_values() {
        return new Object[]{ true, false };
    }

    public static Object[] ComputeCoverage_values() {
        return new Object[]{ true, false };
    }

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
