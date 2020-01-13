package com.emt.steps;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.IDockerNamespace;

import groovy.lang.Closure;

@RunWith(Theories.class)
public class BuildDockerImageTest extends StepTestFixture {

	private boolean _executed = false;
	
	public static List<String> getParameters() {
		return Arrays.asList("image_name", "image_user", "manager_node",
				             "environment", "from_image_name", "force");
	}
	
    public static String[] image_name_values() {
        return new String[]{ "my_docker_image" };
    }
	
    public static Object[] image_user_values() {
        return new Object[]{ "my_image_user", new Unassigned() };
    }
	
    public static Object[] manager_node_values() {
        return new Object[]{ "my_puppet_manager_node", new Unassigned() };
    }
	
    public static Object[] environment_values() {
        return new Object[]{ "my_puppet_environment", new Unassigned() };
    }
	
    public static Object[] from_image_name_values() {
        return new Object[]{ "my_from_image", new Unassigned() };
    }
	
    public static Object[] force_values() {
        return new Object[]{ new Boolean(true), new Boolean(false), new Unassigned() };
    }

	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }

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

    	assert inst().execute(args).equals(new_image);
    }
    
    @Theory
    public void skipsBuildForExistingImage(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == false) ||
			       !args.containsKey("force"));
    	
    	String image_name = (String) args.get("image_name");
    	Object existing_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(true);
        when(_steps.docker.image(image_name)).thenReturn(existing_image);

    	assert inst().execute(args).equals(existing_image);
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
        
    	assert inst().execute(args).equals(new_image);
    }

    @Theory
    public void forceBuild2(@FromDataPoints("args") Map args) {
    	assumeTrue((args.containsKey("force") && ((boolean)args.get("force")) == true));

    	String image_name = (String) args.get("image_name");
    	Object new_image = new Object();
        when(_steps.dockerImageExists(anyString())).thenReturn(false);
        when(_steps.docker.build(eq(image_name), anyString())).thenReturn(new_image);
        
    	assert inst().execute(args).equals(new_image);
    }
}
