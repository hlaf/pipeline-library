package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;

public class GetDnsDomainNameTest extends StepTestFixture {
    
    @Test
    public void returnsValidDomain() {
    	String expected_output = "mydomain.com";
    	when(_steps.sh(any(Map.class))).thenReturn(expected_output);
    	assert inst().execute().equals(expected_output);
    }
    
    @Test
    public void callsErrorOnEmptyDomain() {
    	when(_steps.sh(any(Map.class))).thenReturn("");
    	exception.expect(Exception.class);
    	inst().execute();
    	verify(_steps).error(anyString());
    }

}
