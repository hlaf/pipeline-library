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

@RunWith(Theories.class)
public class SetPackageVersionTest extends StepTestFixture {

	@Parameter(values="1.1.0") String version;
	@Parameter(values="some_version_file") String version_file;
	@Parameter(values={"__version__", "version"}, optional=true) String version_key;

	@StateVar(values={"__version__", "version"}) String file_version_key;
	@StateVar(values={"1.0.0", "10.11.12"}) String file_version_value;
	@StateVar boolean file_contains_version_info;
	
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }
	@DataPoints("state") public static Map[] getState() { return _getState(); }

	private String getFileContent(Map state) {
		String version_info = "";
		if ((boolean)state.get("file_contains_version_info")) {
    	    version_info = String.format("%s = '%s'%n",
    	                    		     state.get("file_version_key"),
    	        		                 state.get("file_version_value"));
		}
		String file_content = String.format(
    			"%nsome random text%n" +
    	        version_info +
    	        "%nmore random text%n"
    	);
		return file_content;
	}

    @Theory
    public void setVersionSucceeds(@FromDataPoints("args") Map args,
    		                       @FromDataPoints("state") Map state) {
    	assumeTrue((boolean)state.get("file_contains_version_info"));
    	assumeTrue(state.get("file_version_key").equals(args.get("version_key")));
   
    	String initial_content = getFileContent(state);
    	assertFalse(initial_content.contains((String) args.get("version")));
    	doReturn(initial_content).when(_steps).readFile(args.get("version_file"));
    	    	
    	inst().execute(args);
    	ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
    	verify(_steps).writeFile(captor.capture());
    	String final_content = (String) captor.getValue().get("text");
    	
    	assertTrue(final_content.contains((String)args.get("version")));
    }
    
    @Theory
    public void failsWhenVersionInfoMissing(@FromDataPoints("args") Map args,
    		                                @FromDataPoints("state") Map state) {
    	assumeFalse((boolean)state.get("file_contains_version_info"));
    	
    	doReturn(getFileContent(state)).when(_steps).readFile(args.get("version_file"));
    	
    	exception.expect(Exception.class);
    	inst().execute(args);
    }

}
