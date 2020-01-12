package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Test;

public class NotifyBuildTest extends StepTestFixture {

    @Test
    public void callsEmailext() {
    	_steps.env.put("JOB_NAME", "dummy_job_name");
    	_steps.env.put("BUILD_NUMBER", "12345");
    	_steps.env.put("BUILD_URL", "https://dummy.jenkins.build.url/dummy_job_name");
    	inst().execute();
        verify(_steps).emailext(any(Map.class));
    }

}
