package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class BumpPackageVersionTest extends StepTestFixture {

	@Parameter(values="my_credentials") String git_repo_creds;
	@Parameter(values="some_author") String author;
	@Parameter(values="some_author@some_domain.com") String author_email;
	
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }

	@Before
    public void setup() {
    	super.setup();
    	_steps.env = new HashMap<>();
    	_steps.env.put("JOB_NAME", "dummy_job");
    }

    @Theory
    public void runsWithoutErrors(@FromDataPoints("args") Map args) {
    	inst().execute(args);
    	verify(_steps).sh(anyString());
    }
}
