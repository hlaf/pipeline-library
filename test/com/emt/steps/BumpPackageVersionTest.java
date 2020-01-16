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

	@Parameter String git_repo_creds;
	@Parameter String author;
	@Parameter String author_email;
	
    public static String[] git_repo_creds_values() {
        return new String[]{ "my_credentials" };
    }
	
    public static Object[] author_values() {
        return new Object[]{ "some_author" };
    }
	
    public static Object[] author_email_values() {
        return new Object[]{ "some_author@some_domain.com" };
    }
	
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
