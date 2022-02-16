package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;


public class WriteAsJsonTest extends StepTestFixture {

	@Parameter(values={"some/file/path"}) String file;
	@Parameter Map json;
	
	public static Object[] json_values() {
	    Map m = new HashMap();
        m.put("foo", "bar");
	    return new Object[] { m };
	}
	
	@Theory
    public void writesDataAsJSON(@FromDataPoints("args") Map args) {
    	execute(args);

    	verify(_steps, times(1)).writeFile(_captor_map.capture());
    	assertTrue(_captor_map.getValue().get("text") instanceof String);
    }

}
