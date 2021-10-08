package com.emt.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;


public class UploadToArtifactRepositoryTest extends StepTestFixture {

	@Parameter(values="my_repo_url") String artifact_repo_url;
	@Parameter(values="my_creds") String artifact_repo_creds;
	
    @Theory
    public void callsSh(@FromDataPoints("args") Map args) {
    	execute(args);
        verify(_steps).sh(anyString());
    }

}
