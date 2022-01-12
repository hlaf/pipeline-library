package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class NotifyBuildTest extends StepTestFixture {

    @Parameter String build_status;
    @Parameter String recipient_email;

    @Theory
    public void callsEmailext(@FromDataPoints("args") Map args) {
        _steps.env.put("JOB_NAME", "dummy_job_name");
        _steps.env.put("BUILD_NUMBER", "12345");
        _steps.env.put("BUILD_URL", "https://dummy.jenkins.build.url/dummy_job_name");
        execute(args);
        verify(_steps).emailext(any(Map.class));
    }

}
