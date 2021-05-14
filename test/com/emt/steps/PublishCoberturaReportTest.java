package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;
import com.emt.util.StateVar;

import hudson.AbortException;

@RunWith(Theories.class)
public class PublishCoberturaReportTest extends StepTestFixture {

	@Parameter List<String> results;

	@StateVar boolean StashExists;

    public static Object[] results_values() {
        return new Object[]{ new ArrayList<String>(), 
        		             Arrays.asList("unit", "integration")};
    }
	
	private void commonSetup(Map args, Map state) {
		if ((boolean)state.get("StashExists")) {
			try {
				when(_steps.unstashCoverageResult(any(Map.class))).thenThrow(AbortException.class);
			} catch (AbortException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Theory
    public void resultsArePublishedOnlyOnce(@FromDataPoints("args") Map args,
    		        						@FromDataPoints("state") Map state) {
    	commonSetup(args, state);
    	inst().execute(args);
    	verify(_steps, times(1)).step(any(Map.class));
    }

}
