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
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.emt.util.Parameter;

@RunWith(Theories.class)
public class BumpPackageVersionTest extends StepTestFixture {

	@Parameter(values="my_credentials") String git_repo_creds;
	@Parameter(values="some_author") String author;
	@Parameter(values="some_author@some_domain.com") String author_email;
	
	@Before
    public void setup() {
    	super.setup();
    	_steps.env = new HashMap<>();
    	_steps.env.put("JOB_NAME", "dummy_job");
    }

    @Theory
    public void runsWithoutErrors(@FromDataPoints("args") Map args) {
    	String bumped_version = "1.1.0";
    	doReturn(bumped_version).when(_steps).sh(any(Map.class));
    	inst().execute(args);
    	
    	ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
    	verify(_steps).setPackageVersion(captor.capture());
    	assertTrue(captor.getValue().get("version").equals(bumped_version));    	
    	verify(_steps).sh(anyString());
    }
}
