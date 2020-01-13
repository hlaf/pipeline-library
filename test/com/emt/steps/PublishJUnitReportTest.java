package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class PublishJUnitReportTest extends StepTestFixture {

	@Theory
    public void callsJunit() {
    	inst().execute();
    	verify(_steps).junit(any(Map.class));
    }
}
