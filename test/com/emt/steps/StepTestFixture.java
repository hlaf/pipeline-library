package com.emt.steps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StepTestFixture {

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
