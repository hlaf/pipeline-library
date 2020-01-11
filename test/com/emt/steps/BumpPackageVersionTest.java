package com.emt.steps;

import static com.google.common.collect.Sets.cartesianProduct;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class BumpPackageVersionTest extends StepTestFixture {

	@DataPoints("git_repo_creds")
    public static String[] git_repo_creds_values() {
        return new String[]{ "my_credentials" };
    }
	
	@DataPoints("author")
    public static Object[] author_values() {
        return new Object[]{ "some_author" };
    }
	
	@DataPoints("author_email")
    public static Object[] author_email_values() {
        return new Object[]{ "some_author@some_domain.com" };
    }
	
	@DataPoints("args")
	public static Map[] getArgs() {
		
		Set<List<Object>> s = cartesianProduct(
				new HashSet<Object>(Arrays.asList(git_repo_creds_values())),
				new HashSet<Object>(Arrays.asList(author_values())),
				new HashSet<Object>(Arrays.asList(author_email_values()))
				);
		
		Map[] arg_combinations = new Map[s.size()];
		
		int i = 0;
		for (List<Object> tuple : s) {
			System.out.println(tuple);
			Map<String, Object> m = new HashMap<>();
			arg_combinations[i++] = m;
			putValue(m, "git_repo_creds", tuple.get(0));
			putValue(m, "author", tuple.get(1));
			putValue(m, "author_email", tuple.get(2));
		}

		return arg_combinations;
	}

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
