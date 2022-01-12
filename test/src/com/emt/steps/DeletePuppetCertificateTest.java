package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class DeletePuppetCertificateTest extends StepTestFixture {

    @Parameter
    String certificate_name;
    @Parameter(optional = true)
    String manager_node;
    @Parameter(optional = true)
    String master;
    @Parameter(optional = true)
    String environment;
    @Parameter(optional = true)
    String ssl_cert_path;
    @Parameter(optional = true)
    String ssl_cert_key;
    @Parameter(optional = true)
    String puppetapi_cert_name;

    @Theory
    public void callsShWithCommand(@FromDataPoints("args") Map args) {
        execute(args);
        verify(_steps).sh(anyString());
    }

}
