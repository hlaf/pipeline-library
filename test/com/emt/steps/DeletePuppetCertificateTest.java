package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class DeletePuppetCertificateTest extends StepTestFixture {

    public DeletePuppetCertificate inst() {
    	return new DeletePuppetCertificate(_steps);
    }
    
    @Test
    public void callsShWithCommand() {
        inst().execute("mycertificate.com");
        verify(_steps).sh(anyString());
    }

}
