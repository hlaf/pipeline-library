package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;


public class StashCoverageResultTest extends StepTestFixture {

	@Parameter(values="my_results_key") String key;
	
	@Theory
    public void callsSaferStash(@FromDataPoints("args") Map args) {
    	inst().execute(args);
    	verify(_steps, times(1)).saferStash(any(Map.class));
    }

}
