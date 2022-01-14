package com.emt.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.emt.util.Parameter;
import com.emt.util.StateVar;

import static com.emt.util.TestUtils.getFileContent;


public class SetPackageVersionTest extends StepTestFixture {

	@Parameter(values="1.1.0") String version;
	@Parameter(values="some_version_file") String version_file;
	@Parameter(values={"__version__", "version"}, optional=true) String version_key;

	@StateVar(values={"__version__", "version"}) String file_version_key;
	@StateVar(values={"1.0.0", "10.11.12"}) String file_version_value;
	@StateVar boolean file_contains_version_info;
	
	protected void commonSetup(Map args, Map state) {
    	doReturn(getFileContent(state)).when(_steps).readFile(args.get("version_file"));
	}
	
    @Theory
    public void setVersionSucceeds(@FromDataPoints("args") Map args,
    		                       @FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("file_contains_version_info"));
    	assumeTrue(state.get("file_version_key").equals(args.get("version_key")));
   
    	String initial_content = getFileContent(state);
    	assertFalse(initial_content.contains((String) args.get("version")));

    	commonSetup(args, state);	
    	execute(args);
    	ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
    	verify(_steps).writeFile(captor.capture());
    	String final_content = (String) captor.getValue().get("text");
    	
    	assertTrue(final_content.contains((String)args.get("version")));
    }
    
    @Theory
    public void failsWhenVersionInfoMissing(@FromDataPoints("args") Map args,
    		                                @FromDataPoints("state") Map state) {
    	assumeFalse((boolean)state.get("file_contains_version_info"));
    	
    	exception.expect(Exception.class);
    	commonSetup(args, state);	
    	execute(args);
    }

}
