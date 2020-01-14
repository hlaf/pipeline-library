package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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
public class StashCoverageResultTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("key");
	}

    public static String[] key_values() {
        return new String[]{ "my_results_key" };
    }
	
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }

	private void commonSetup(Map args) {
//		when(_steps.fileExists((String)args.get("to"))).thenReturn((boolean)state.get("UnstashPathExists"));
//    	when(_steps.fileExists(endsWith(SaferUnstash.METADATA_FILE_NAME))).thenReturn((boolean)state.get("MetadataExists"));
//		_steps.env.put("BUILD_NUMBER", state.get("build_number").toString());
//		Map props = new HashMap();
//    	props.put("stash_name", state.get("metadata_stash_name"));
//    	props.put("build_number", state.get("metadata_build_number"));
//		when(_steps.readJSON(any(Map.class))).thenReturn(props);
	}
	
	@Theory
    public void callsSaferStash(@FromDataPoints("args") Map args) {
    	commonSetup(args);
    	inst().execute(args);
    	verify(_steps, times(1)).saferStash(any(Map.class));
    }

}
