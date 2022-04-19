package com.emt.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.experimental.theories.Theory;

public class GetPipelineConfigTest extends StepTestFixture {

    @Theory
    public void usesSeparateNamespacesPerJob() {
    	// Object identity tests

    	// Use namespace 1
    	_steps.env["JOB_NAME"] = "test_job_name_1"
    	_steps.env["BUILD_NUMBER"] = "123"
        Map config1 = execute();
        assertTrue(config1.size() == 0);
        config1.put("a", 1);
        assertTrue(config1.size() == 1);
        assertTrue(execute().is(config1));
        assertTrue(config1.size() == 1);
        
        // Switch to namespace 2
    	_steps.env["BUILD_NUMBER"] = "124"
    	Map config2 = execute()
    	assertTrue(config2.size() == 0);
    	assertFalse(execute().is(config1))
    	assertTrue(execute().is(config2))
        
    	// Go back to namespace 1
    	_steps.env["BUILD_NUMBER"] = "123"
    	assertTrue(execute().is(config1));

    	// Switch to namespace 3
    	_steps.env["JOB_NAME"] = "test_job_name_2"
    	assertFalse(execute().is(config1))
    	assertFalse(execute().is(config2))
    	assertTrue(execute().is(execute()))
    }

}
