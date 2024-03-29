package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;
import com.emt.util.StateVar;


public class SaferUnstashTest extends StepTestFixture {

	@Parameter(values={"my_stash_key"}) String key;
	@Parameter(values={"some/dummy/unstash/path"}) String to;
	
	@StateVar boolean UnstashPathExists;
	@StateVar boolean MetadataExists;
	@StateVar(values={"my_stash_key", "someone_elses_stash"}) String metadata_stash_name;
	@StateVar(values={"1", "2"}) String metadata_build_number;
	@StateVar(values={"1", "2"}) String build_number;
	
	protected void commonSetup(Map args, Map state) {
		when(_steps.fileExists((String)args.get("to"))).thenReturn((boolean)state.get("UnstashPathExists"));
    	when(_steps.fileExists(endsWith(SaferUnstash.METADATA_FILE_NAME))).thenReturn((boolean)state.get("MetadataExists"));
		_steps.env.put("BUILD_NUMBER", state.get("build_number").toString());
		Map props = new HashMap();
    	props.put("stash_name", state.get("metadata_stash_name"));
    	props.put("build_number", state.get("metadata_build_number"));
		when(_steps.readJSON(any(Map.class))).thenReturn(props);
	}
	
	@Theory
    public void unstashToNewDirectory(@FromDataPoints("args") Map args,
    		                          @FromDataPoints("state") Map state) {
    	assumeFalse((boolean)state.get("UnstashPathExists"));
    	commonSetup(args, state);
    	
    	execute(args);
    	verify(_steps).unstash((String)args.get("key"));
    }

    @Theory
    public void unstashToExistingDirectoryWithoutMetadataFails(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeFalse((boolean)state.get("MetadataExists"));
    	
    	commonSetup(args, state);
    	
    	execute(args);
    	assertTrue(error_was_called());
    }
    
    @Theory
    public void unstashToDirectoryFromDifferentStashFails(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeTrue((boolean)state.get("MetadataExists"));
    	assumeFalse(state.get("metadata_stash_name").equals(args.get("key")));

    	commonSetup(args, state);

    	execute(args);
    	assertTrue(error_was_called());
    }
    
    @Theory
    public void unstashToDirectoryFromPreviousBuildFails(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeTrue((boolean)state.get("MetadataExists"));
    	assumeTrue(state.get("metadata_stash_name").equals(args.get("key")));
    	assumeFalse(state.get("metadata_build_number").equals(state.get("build_number")));

    	commonSetup(args, state);
    	
    	execute(args);
    	assertTrue(error_was_called());
    }
    
    @Theory
    public void unstashToDirectoryFromSameBuildDoesNothing(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeTrue((boolean)state.get("MetadataExists"));
    	assumeTrue(state.get("metadata_stash_name").equals(args.get("key")));
    	assumeTrue(state.get("metadata_build_number").equals(state.get("build_number")));

    	commonSetup(args, state);
    	
    	execute(args);
    	verify(_steps, never()).unstash(anyString());
    }
    
}
