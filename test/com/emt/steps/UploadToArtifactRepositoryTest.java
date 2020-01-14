package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class UploadToArtifactRepositoryTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("artifact_repo_url", "artifact_repo_creds");
	}

    public static String[] artifact_repo_url_values() {
        return new String[]{ "my_repo_url" };
    }
	
    public static Object[] artifact_repo_creds_values() {
        return new Object[]{ "my_creds" };
    }

    @DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
    
    @Theory
    public void callsSh(@FromDataPoints("args") Map args) {
    	inst().execute();
        verify(_steps).sh(anyString());
    }

}
