package com.emt.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class GetPipelineConfigTest extends StepTestFixture {

    @Test
    public void usesSeparateNamespacesPerJob() {
    	// Object identity tests

    	// Use namespace 1
    	_steps.env.put("JOB_NAME", "test_job_name_1");
    	_steps.env.put("BUILD_NUMBER", "123");
        Map config1 = (Map) inst().execute();
        assertTrue(config1.size() == 0);
        config1.put("a", 1);
        assertTrue(inst().execute() == config1);
        
        // Switch to namespace 2
    	_steps.env.put("BUILD_NUMBER", "124");
    	Map config2 = (Map) inst().execute();
    	assertTrue(config2.size() == 0);
    	assertFalse(inst().execute() == config1);
    	assertTrue(inst().execute() == config2);
        
    	// Go back to namespace 1
    	_steps.env.put("BUILD_NUMBER", "123");
    	assertTrue(inst().execute().equals(config1));

    	// Switch to namespace 3
    	_steps.env.put("JOB_NAME", "test_job_name_2");
    	assertFalse(inst().execute() == config1);
    	assertFalse(inst().execute() == config2);
    	assertTrue(inst().execute() == inst().execute());    	
    }

}
