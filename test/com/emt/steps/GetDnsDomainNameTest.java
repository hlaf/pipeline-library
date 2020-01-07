package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emt.IStepExecutor;
import com.emt.ioc.IContext;

public class GetDnsDomainNameTest extends StepTestFixture {
    private IContext _context;
    private IStepExecutor _steps;

    @Before
    public void setup() {
        _context = mock(IContext.class);
        _steps = mock(IStepExecutor.class, Mockito.CALLS_REAL_METHODS);

        when(_context.getStepExecutor()).thenReturn(_steps);
    }

    public GetDnsDomainName inst() {
    	return new GetDnsDomainName(_steps);
    }
    
    @Test
    public void returnsValidDomain() {
    	String expected_output = "mydomain.com";
    	when(_steps.sh(any(Map.class))).thenReturn(expected_output);
    	assert inst().execute().equals(expected_output);
    }
    
    @Test
    public void callsErrorOnEmptyDomain() {
    	when(_steps.sh(any(Map.class))).thenReturn("");
    	inst().execute();
    	verify(_steps).error(anyString());
    }
    
}
