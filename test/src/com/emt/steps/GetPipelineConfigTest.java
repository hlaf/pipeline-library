package com.emt.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.experimental.theories.Theory;

public class GetPipelineConfigTest extends StepTestFixture {

    @Theory
    public void usesSeparateNamespacesPerJob() {
    	// Object identity tests

    	// Use namespace 1
    	_steps.env.put("JOB_NAME", "test_job_name_1");
    	_steps.env.put("BUILD_NUMBER", "123");
        Map config1 = (Map) execute(new HashMap<>());
        assertTrue(config1.size() == 0);
        config1.put("a", 1);
        assertTrue(execute(new HashMap<>()) == config1);
        
        // Switch to namespace 2
    	_steps.env.put("BUILD_NUMBER", "124");
    	Map config2 = (Map) execute(new HashMap<>());
    	assertTrue(config2.size() == 0);
    	assertFalse(execute(new HashMap<>()) == config1);
    	assertTrue(execute(new HashMap<>()) == config2);
        
    	// Go back to namespace 1
    	_steps.env.put("BUILD_NUMBER", "123");
    	assertTrue(execute(new HashMap<>()).equals(config1));

    	// Switch to namespace 3
    	_steps.env.put("JOB_NAME", "test_job_name_2");
    	assertFalse(execute(new HashMap<>()) == config1);
    	assertFalse(execute(new HashMap<>()) == config2);
    	assertTrue(execute(new HashMap<>()) == execute(new HashMap<>()));    	
    }

}
