package com.emt.steps;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.junit.Before;
import org.mockito.Mockito;

import com.emt.IStepExecutor;
import com.emt.ioc.IContext;

class Unassigned {};

public abstract class StepTestFixture {

	private IContext _context;
    protected IStepExecutor _steps;
	protected boolean _executed = false;
    
    public final Class<? extends BaseStep> getStepClass() {
    	String test_class_name = this.getClass().getName();
    	String step_class_name = test_class_name.substring(0, test_class_name.lastIndexOf("Test"));
    	Class<? extends BaseStep> klass;
    	try {
			klass = (Class<? extends BaseStep>) Class.forName(step_class_name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    	return klass;
    }
    
	@Before
    public void setup() {
        _context = mock(IContext.class);
        _steps = mock(IStepExecutor.class, Mockito.CALLS_REAL_METHODS);

        when(_context.getStepExecutor()).thenReturn(_steps);
    }
	
	public final BaseStep inst() {
		try {
			return getStepClass().getConstructor(Object.class).newInstance(_steps);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void putValue(Map args, String key, Object value) {
		if (value instanceof Unassigned) return;
		args.put(key, value);
	}
	
	protected final Object execute(Map args) {
		try {
			return inst().execute(args);
		} finally {
			_executed = true;
		}
	}
	
}