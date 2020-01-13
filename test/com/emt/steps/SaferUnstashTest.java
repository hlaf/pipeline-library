package com.emt.steps;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class SaferUnstashTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("key", "to");
	}

	public static List<String> getStateVariables() {
		return Arrays.asList("UnstashPathExists",
				             "MetadataExists",
				             "metadata_stash_name",
				             "metadata_build_number",
				             "build_number");
	}

    public static String[] key_values() {
        return new String[]{ "my_stash_key" };
    }
	
    public static Object[] to_values() {
        return new Object[]{ "some/dummy/unstash/path" };
    }

    public static Object[] metadata_stash_name_values() {
        return new Object[]{ "my_stash_key", "someone_elses_stash" };
    }
    
    public static Object[] metadata_build_number_values() {
        return new Object[]{ "1", "2" };
    }

    public static Object[] build_number_values() {
        return new Object[]{ "1", "2" };
    }
    
    public static Object[] UnstashPathExists_values() {
    	return new Object[]{ true, false };
    }

    public static Object[] MetadataExists_values() {
    	return new Object[]{ true, false };
    }
    
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }

	@DataPoints("state") public static Map[] getState() { return _getState(); }
	
	@Before
    public void setup() {
    	super.setup();
    }

	private void commonSetup(Map args, Map state) {
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
    	
    	inst().execute(args);
    	verify(_steps).unstash((String)args.get("key"));
    }

    @Theory
    public void unstashToExistingDirectoryWithoutMetadataFails(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeFalse((boolean)state.get("MetadataExists"));
    	
    	commonSetup(args, state);
    	
    	exception.expect(Exception.class);
    	inst().execute(args);
    	verify(_steps).error(anyString());
    }
    
    @Theory
    public void unstashToDirectoryFromDifferentStashFails(
    				@FromDataPoints("args") Map args,
    				@FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("UnstashPathExists"));
    	assumeTrue((boolean)state.get("MetadataExists"));
    	assumeFalse(state.get("metadata_stash_name").equals(args.get("key")));

    	commonSetup(args, state);
    	
    	exception.expect(Exception.class);
    	inst().execute(args);
    	verify(_steps).error(anyString());
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
    	
    	exception.expect(Exception.class);
    	inst().execute(args);
    	verify(_steps).error(anyString());
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
    	
    	inst().execute(args);
    	verify(_steps, never()).unstash(anyString());
    }
    
}
