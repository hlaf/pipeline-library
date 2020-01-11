package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class DeletePuppetCertificateTest extends StepTestFixture {

    @Test
    public void callsShWithCommand() {
        inst().execute(Maps.newHashMap(ImmutableMap.of("certificate_name", "mycertificate.com")));
        verify(_steps).sh(anyString());
    }

}
