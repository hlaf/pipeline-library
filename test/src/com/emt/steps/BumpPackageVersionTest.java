package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;


public class BumpPackageVersionTest extends StepTestFixture {

	@Parameter(values="my_credentials") String git_repo_creds;
	@Parameter(values="some_author") String author;
	@Parameter(values="some_author@some_domain.com") String author_email;
	
	String bumped_version = "1.1.0";
	
	@Before
    public void setup() {
    	super.setup();
    	_steps.env.put("JOB_NAME", "dummy_job");

    	doReturn(bumped_version).when(_steps).sh(any(Map.class));
    }

    @Theory
    public void runsWithoutErrors(@FromDataPoints("args") Map args) {
    	execute(args);

    	verify(_steps).setPackageVersion(_captor_map.capture());
    	assertTrue(_captor_map.getValue().get("version").equals(bumped_version));    	
    	verify(_steps).sh(anyString());
    }
}
