package com.emt.steps;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.emt.util.Parameter;

import groovy.lang.Closure;


public class TempDirTest extends StepTestFixture {

	@Parameter Closure body;
	
	public static Closure noop_closure = new Closure(null) {
		public Object call() { return 1234; }
	};

	public static Closure throwing_closure = new Closure(null) {
		public Object call() { throw new RuntimeException("Exception from throwing closure"); }
	};

    public static Object[] body_values() {
    	return new Object[] { noop_closure, throwing_closure };
    }

	private void commonSetup(Map args) {
		if (closureThrows((Closure)args.get("body"))) {
			exception.expect(RuntimeException.class);
		}
	}

	@Theory
    public void returnsClosureReturnValue(@FromDataPoints("args") Map args) {
    	assumeFalse(closureThrows((Closure)args.get("body")));
		commonSetup(args);
    	execute(args).equals(1234);
    	verify(_steps, times(1)).deleteDir();
    }

	@Theory
	public void deletesDirectoryEvenWhenClosureThrows(@FromDataPoints("args") Map args) {
		assumeTrue(closureThrows((Closure)args.get("body")));
		commonSetup(args);
    	execute(args);
    	verify(_steps, times(1)).deleteDir();
    }
	
}
