package com.emt.steps;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.Mockito;

import com.emt.IStepExecutor;
import com.emt.ioc.IContext;

class Unassigned {};

public class StepTestFixture {

	private IContext _context;
    protected IStepExecutor _steps;
	
    
	@Before
    public void setup() {
        _context = mock(IContext.class);
        _steps = mock(IStepExecutor.class, Mockito.CALLS_REAL_METHODS);

        when(_context.getStepExecutor()).thenReturn(_steps);
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
