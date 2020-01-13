package com.emt.steps;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class UnstashCoverageResultTest extends StepTestFixture {

    @Test
    public void callsEchoWithDefaultMessage() {
    	String key = "my_results_key";
    	String result = (String) inst().execute(ImmutableMap.of("key", key));
    	
    	assert result.endsWith(key);
    	assert result.contains(UnstashCoverageResult.COVERAGE_RESULTS_BASE_DIR);
    }
    
}
