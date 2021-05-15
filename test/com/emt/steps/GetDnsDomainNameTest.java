package com.emt.steps;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.StateVar;

public class GetDnsDomainNameTest extends StepTestFixture {

    @StateVar String domain_name;

    @Theory
    public void returnsValidDomain(@FromDataPoints("state") Map state) {
        assumeFalse(state.get("domain_name").toString().isEmpty());
    	when(_steps.sh(any(Map.class))).thenReturn(state.get("domain_name"));
    	assert inst().execute().equals(state.get("domain_name"));
    }

    @Theory
    public void callsErrorOnEmptyDomain(@FromDataPoints("state") Map state) {
        assumeTrue(state.get("domain_name").toString().isEmpty());
    	when(_steps.sh(any(Map.class))).thenReturn(state.get("domain_name"));
    	exception.expect(Exception.class);
    	inst().execute();
    	verify(_steps).error(anyString());
    }

}
