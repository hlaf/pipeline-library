package com.emt.steps;

import static com.google.common.collect.Sets.cartesianProduct;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.emt.IStepExecutor;
import com.emt.ioc.IContext;

import groovy.lang.Closure;

class Unassigned {};

public abstract class StepTestFixture {

	private IContext _context;
    protected IStepExecutor _steps;
	protected boolean _executed = false;
    
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
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
    
    private final static List<String> _get_parameter_names(Class<?> klass, 
    													   String method_name) {
    	Method m;
    	try {
			m = klass.getMethod(method_name);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}

    	List<String> parameter_names;
		try {
			parameter_names = (List<String>) m.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return parameter_names;
    }
    
    public static Map[] _getState() { return _getArgs(false); }
    
    public static Map[] _getArgs() { return _getArgs(true); }

    private static Map[] _getArgs(boolean input_parameters) {
		List<Set<Object>> input_sets = new ArrayList<Set<Object>>();
		
		String klass_name = Thread.currentThread().getStackTrace()[3].getClassName();
		Class<?> klass;
		try {
			klass = Class.forName(klass_name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		List<String> parameter_names = _get_parameter_names(
				klass, input_parameters ? "getParameters" : "getStateVariables");
		
		for (String parameter_name: parameter_names) {
			Method getter;
			try {
				getter = klass.getMethod(parameter_name + "_values");
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
			Set<Object> input_set = new HashSet<>();
			try {
				Object[] parameter_values = (Object[]) getter.invoke(null);
				input_set.addAll(Arrays.asList(parameter_values));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			input_sets.add(input_set);
		}
		
		Set<List<Object>> s = cartesianProduct(input_sets);
		
		Map[] arg_combinations = new Map[s.size()];
		int i = 0;
		for (List<Object> tuple : s) {
			Map<String, Object> m = new HashMap<>();
			arg_combinations[i++] = m;
			for (int j = 0; j < tuple.size(); ++j) {
				putValue(m, parameter_names.get(j), tuple.get(j));
			}
		}

		return arg_combinations;
	}
    
	@Before
    public void setup() {
        _context = mock(IContext.class);
        _steps = mock(IStepExecutor.class, Mockito.CALLS_REAL_METHODS);
        _steps.env = new HashMap<String, String>();

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
	
	public static boolean closureThrows(Closure c) {
		boolean threw_exception = false;
		try {
			c.call();
		} catch (Exception e) {
			threw_exception = true;
		}
		return threw_exception;
	}
	
}