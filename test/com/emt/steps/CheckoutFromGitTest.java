package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Test;

public class CheckoutFromGitTest extends StepTestFixture {
    
    @Test
    public void callsCheckout() {
    	inst().execute();
    	verify(_steps).checkout(any(Map.class));    	
    }

}
