package com.emt.steps;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.IDockerNamespace;

import groovy.lang.Closure;

@RunWith(Theories.class)
public class BuildDockerImageTest extends StepTestFixture {

	@Parameter(values={"my_docker_image"}) String image_name;
	@Parameter(values={"my_image_user"}, optional=true) String image_user;
	@Parameter(values={"my_puppet_manager_node"}, optional=true) String manager_node;
	@Parameter(values={"my_puppet_environment"}, optional=true) String environment;
	@Parameter(values={"my_puppet_master"}, optional=true) String master;
	@Parameter(values={"my_from_image"}, optional=true) String from_image_name;
	@Parameter(optional=true) boolean force;
	
    @Before
    public void setup() {
    	super.setup();
    	_steps.docker = mock(IDockerNamespace.class);
        when(_steps.getDnsDomainName()).thenReturn("some.valid.domain.com");
    }

    @After
    public void check_invariants() {
    	if (_executed) {
    		runsInDockerNode();
    	}
    }

    public void runsInDockerNode() {
        verify(_steps).node(eq("docker-slave"), isA(Closure.class));
    }

    @Theory
    public void buildsNewImageWhenNoneExists(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == false) ||
    			   !args.containsKey("force"));
    	
    	Object new_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(false);
        when(_steps.docker.build((String) eq(args.get("image_name")), anyString())).thenReturn(new_image);

    	assert execute(args).equals(new_image);
    }
    
    @Theory
    public void skipsBuildForExistingImage(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == false) ||
			       !args.containsKey("force"));
    	
    	String image_name = (String) args.get("image_name");
    	Object existing_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(true);
        when(_steps.docker.image(image_name)).thenReturn(existing_image);

    	assert execute(args).equals(existing_image);
    }

    @Theory
    public void forceBuild1(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == true));
    	
    	String image_name = (String) args.get("image_name");
    	Object existing_image = new Object();
    	Object new_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(true);
        when(_steps.docker.image(image_name)).thenReturn(existing_image);
        when(_steps.docker.build(eq(image_name), anyString())).thenReturn(new_image);
        
    	assert execute(args).equals(new_image);
    }

    @Theory
    public void forceBuild2(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == true));

    	String image_name = (String) args.get("image_name");
    	Object new_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(false);
        when(_steps.docker.build(eq(image_name), anyString())).thenReturn(new_image);
        
    	assert execute(args).equals(new_image);
    }
}
