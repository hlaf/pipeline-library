package com.emt.steps;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GeneralStepTests {

	@Test
	//@Ignore("Not implemented yet")
	public void allPipelineStepsHaveTests() {
		for (String step_name : getStepNames()) {
			String test_class_name = step_name + "Test";
			try {
				Class.forName(test_class_name);
			} catch (ClassNotFoundException e) {
				fail(String.format("Could not find test class: %s", test_class_name));
			}
		}
	}

	public static String capitalize(String str) {
		String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
		return cap;
	}
	
	public static List<String> getStepNames() {
		String package_name = "com.emt.steps.";
		List<String> step_names = new ArrayList<String>();
        for (String pathname : new File("vars").list()) {
            step_names.add(package_name + capitalize(pathname).split(".groovy")[0]);
        }
        return step_names;
	}
	
}
