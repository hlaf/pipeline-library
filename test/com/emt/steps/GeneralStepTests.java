package com.emt.steps;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

public class GeneralStepTests extends StepTestFixture {

	@Test
	@Ignore("Not implemented yet")
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

}
