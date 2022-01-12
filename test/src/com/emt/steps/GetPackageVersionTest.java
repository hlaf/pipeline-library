package com.emt.steps;

import static com.emt.util.TestUtils.getFileContent;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.doReturn;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;
import com.emt.util.StateVar;


public class GetPackageVersionTest extends StepTestFixture {

	@Parameter(values="some_version_file") String version_file;
	@Parameter(values={"__version__", "version"}, optional=true) String version_key;

	@StateVar(values={"__version__", "version"}) String file_version_key;
	@StateVar(values={"1.0.0", "10.11.12"}) String file_version_value;
	@StateVar boolean file_contains_version_info;
	
    @Theory
    public void findsVersion(@FromDataPoints("args") Map args,
    		                 @FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("file_contains_version_info"));
    	assumeTrue(state.get("file_version_key").equals(args.get("version_key")));
    	
    	doReturn(getFileContent(state)).when(_steps).readFile(args.get("version_file"));
    	
    	String result = (String) execute(args);
    	assertTrue(result.equals((String) state.get("file_version_value")));
    }
    
    @Theory
    public void failsWhenVersionKeyDiffers(@FromDataPoints("args") Map args,
    		                               @FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("file_contains_version_info"));
    	assumeTrue(args.containsKey("version_key"));
    	assumeFalse(state.get("file_version_key").equals(args.get("version_key")));
    	
    	doReturn(getFileContent(state)).when(_steps).readFile(args.get("version_file"));
    	
    	execute(args);
    	assertTrue(error_was_called());
    }
    
    @Theory
    public void failsWhenVersionInfoMissing(@FromDataPoints("args") Map args,
    		                                @FromDataPoints("state") Map state) {
    	assumeFalse((boolean)state.get("file_contains_version_info"));
    	
    	doReturn(getFileContent(state)).when(_steps).readFile(args.get("version_file"));
    	
    	execute(args);
    	assertTrue(error_was_called());
    }

}
