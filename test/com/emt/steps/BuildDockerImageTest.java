package com.emt.steps;

import static com.google.common.collect.Sets.cartesianProduct;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	@DataPoints("image_name")
    public static String[] image_names() {
        return new String[]{ "my_docker_image" };
    }
	
	@DataPoints("image_user")
    public static Object[] image_users() {
        return new Object[]{ "my_image_user", new Unassigned() };
    }
	
	@DataPoints("manager_node")
    public static Object[] manager_nodes() {
        return new Object[]{ "my_puppet_manager_node", new Unassigned() };
    }
	
	@DataPoints("environment")
    public static Object[] environments() {
        return new Object[]{ "my_puppet_environment", new Unassigned() };
    }
	
	@DataPoints("from_image_name")
    public static Object[] from_image_names() {
        return new Object[]{ "my_from_image", new Unassigned() };
    }
	
	@DataPoints("force")
    public static Object[] force_values() {
        return new Object[]{ true, false, new Unassigned() };
    }

	@DataPoints("args")
	public static Map[] getArgs() {
		
		Set<List<Object>> s = cartesianProduct(
				new HashSet<Object>(Arrays.asList(image_names())),
				new HashSet<Object>(Arrays.asList(image_users())),
				new HashSet<Object>(Arrays.asList(manager_nodes())),
				new HashSet<Object>(Arrays.asList(environments())),
				new HashSet<Object>(Arrays.asList(from_image_names())),
		        new HashSet<Object>(Arrays.asList(force_values()))   
				);
		
		Map[] arg_combinations = new Map[s.size()];
		
		int i = 0;
		for (List<Object> tuple : s) {
			System.out.println(tuple);
			Map<String, Object> m = new HashMap<>();
			arg_combinations[i++] = m;
			putValue(m, "image_name", tuple.get(0));
			putValue(m, "image_user", tuple.get(1));
			putValue(m, "manager_node", tuple.get(2));
			putValue(m, "environment", tuple.get(3));
			putValue(m, "from_image_name", tuple.get(4));
			putValue(m, "force", tuple.get(5));
		}

		return arg_combinations;
	}
	
	@Override
	public Class<? extends BaseStep> getStepClass() { return BuildDockerImage.class; }
	
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
