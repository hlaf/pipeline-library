package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class PublishJUnitReportTest extends StepTestFixture {

    @Parameter(values={"**/some_pattern.xml"}, optional=true)
    String test_results;

    @Theory
    public void callsJunit(@FromDataPoints("args") Map args) {
        inst().execute(args);
        verify(_steps).junit(any(Map.class));
    }
}
