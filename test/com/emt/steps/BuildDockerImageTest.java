package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.emt.IDockerNamespace;

import groovy.lang.Closure;

public class BuildDockerImageTest extends StepTestFixture {

    public BuildDockerImage inst() {
    	return new BuildDockerImage(_steps);
    }
    
    @Before
    public void setup() {
    	super.setup();
    	_steps.docker = mock(IDockerNamespace.class);
        when(_steps.getDnsDomainName()).thenReturn("some.valid.domain.com");
    }
    
    @Test
    public void runsInDockerNode() {
        inst().execute();
        verify(_steps).node(eq("docker-slave"), isA(Closure.class));
    }

    @Test
    public void skipsBuildForExistingImage() {
    	Map args = new HashMap();
    	args.put("image_name", "ole");
        when(_steps.dockerImageExists(anyString())).thenReturn(true);
    	inst().execute(args);
        verify(_steps).node(eq("docker-slave"), isA(Closure.class));
    }
    
}
