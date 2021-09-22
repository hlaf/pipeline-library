package com.emt.steps;

import java.util.HashMap;
import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

import groovy.lang.Closure;


public class RunParallelTest extends StepTestFixture {

	@Parameter Map<String, Closure> tasks;
	@Parameter(values={"1", "2"}) int n_workers;

    public static Closure noop_closure = new Closure(null) {
        public Object call() { return 1234; }
    };

    public static Object[] tasks_values() {
        
        Map tasks = new HashMap<String, Closure>();

        tasks.put("task_1", noop_closure);
        
        return new Object[] {
          tasks,
        };
    }

	//@Parameter(values={"my_label"}, optional=true) String label;

	@Theory
    public void testsAreRun(@FromDataPoints("args") Map args) {
    	inst().execute(args);
    	//verify(_steps).publishJUnitReport();
    }

}
