package com.emt.steps;

import static com.google.common.collect.Sets.cartesianProduct;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.emt.IStepExecutor;
import com.emt.common.MissingArgumentException;
import com.emt.ioc.IContext;
import com.emt.util.Parameter;
import com.emt.util.StateVar;

import groovy.lang.Closure;

class Unassigned {
};

class NullValue {
};

class ReflectionUtils {
    
    public static List<String> getFieldNamesForAnnotation(Class target_klass, Class annotation_class) {

        List<String> parameter_names = new ArrayList<>();

        for (Field field : target_klass.getDeclaredFields()) {
            String name = field.getName();
            if (field.isAnnotationPresent(annotation_class)) {
                parameter_names.add(name);
            }
        }
        return parameter_names;
    }

    public static boolean isStrictSubclassOf(Class<?> child, Class<?> parent) {
        return parent.isAssignableFrom(child) && !child.isAssignableFrom(parent);
    }

    public static Field getField(Class klass, String field_name) {
        Field f;
        try {
            f = klass.getDeclaredField(field_name);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
        return f;
    }

    public static boolean hasField(Class klass, String field_name) {
        return getField(klass, field_name) != null;
    }

    public static Method getMethod(Class klass, String method_name) {
        Method m;
        try {
            m = klass.getMethod(method_name);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
        return m;
    }

    public static boolean hasMethod(Class klass, String method_name) {
        return getMethod(klass, method_name) != null;
    }

}

@RunWith(Theories.class)
public abstract class StepTestFixture {

    private IContext _context;
    protected IStepExecutor _steps;
    protected boolean _executed = false;
    protected Map _state, _args;

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

    public static Map[] _getState() {
        return _getArgs(false);
    }

    public static Map[] _getArgs() {
        return _getArgs(true);
    }

    private static Class<?> getTestClass() {
        StackTraceElement[] stack_trace = Thread.currentThread().getStackTrace();
        
        Class<?> test_klass;
        for (int i=1; i < stack_trace.length; ++i) {
            String klass_name = stack_trace[i].getClassName();
            try {
                test_klass = Class.forName(klass_name);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (ReflectionUtils.isStrictSubclassOf(test_klass, StepTestFixture.class)) {
                return test_klass;
            }
        }
        throw new RuntimeException("Could not find the test class");
    }

    private static Map[] _getArgs(boolean input_parameters) {
        List<Set<Object>> input_sets = new ArrayList<Set<Object>>();

        Class<?> klass = getTestClass();
        Class<? extends Annotation> annotation_klass = input_parameters ? Parameter.class : StateVar.class;

        List<String> parameter_names = ReflectionUtils.getFieldNamesForAnnotation(klass, annotation_klass);

        for (String parameter_name : parameter_names) {

            Set<Object> input_set = new HashSet<>();

            String getter_name = parameter_name + "_values";
            if (ReflectionUtils.hasMethod(klass, getter_name)) {
                input_set.addAll(getParameterValuesFromGetter(klass, getter_name));
            } else if (ReflectionUtils.hasField(klass, parameter_name)) {
                Field f = ReflectionUtils.getField(klass, parameter_name);
                Class type = f.getType();

                boolean is_optional = false;
                Object[] values;
                if (input_parameters) {
                    Parameter p = (Parameter) f.getAnnotation(annotation_klass);
                    values = p.values();
                    is_optional = p.optional();
                } else {
                    StateVar p = (StateVar) f.getAnnotation(annotation_klass);
                    values = p.values();
                }

                for (Object o : values) {
                    try {
                        if (type.equals(double.class)) {
                            type = Double.class;
                        }
                        input_set.add(type.getConstructor(o.getClass()).newInstance(o));
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                    input_set.add(Boolean.TRUE);
                    input_set.add(Boolean.FALSE);
                } else if (type.equals(String.class) && input_set.isEmpty()) {
                    input_set.add(parameter_name + "_test_string");
                    // test the empty string
                    input_set.add("");
                }

                if (input_set.isEmpty()) {
                    throw new RuntimeException("Cannot generate parameter values");
                }

                if (is_optional) {
                    input_set.add(new Unassigned());
                }
            } else {
                throw new RuntimeException("No parameter values!");
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

    private static boolean isEmptyArgSet(Map<String, Object> params) {
        return params.size() == 1 && params.containsKey(".");
    }

    private static Map getEmptyArgSet() {
        Map null_map = new HashMap();
        null_map.put(".", new NullValue());
        return null_map;
    }

    //private static Collection<String> getRequiredParameters() {
    //    
    //}
    
    public static Map[] _getInputWithMissingArgs() { // Mutate input
        Map<String, Object>[] input_args = _getArgs();
        for (int i=0; i < input_args.length; ++i) {
            Map<String, Object> m = input_args[i];
            List<String> args_to_remove = new ArrayList<String>();
            for (String parameter_name: m.keySet()) {
                Field f = ReflectionUtils.getField(getTestClass(), parameter_name);
                Parameter parameter_info = f.getAnnotation(Parameter.class);
                if (!parameter_info.optional()) {
                    args_to_remove.add(parameter_name);
                }
            }
            for (String p: args_to_remove) { m.remove(p); }
        }

        if (input_args.length == 0) {
            return new Map[] { getEmptyArgSet() };
        } else {
            return input_args;
        }
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
            BaseStep inst = spy(getStepClass().getConstructor(Object.class).newInstance(_steps));
            postInst(inst);
            return inst;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void postInst(BaseStep inst) {
    }

    public static void putValue(Map args, String key, Object value) {
        if (value instanceof Unassigned)
            return;
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

    private static List<Object> getParameterValuesFromGetter(Class<?> klass, String getter_name) {
        Method getter = ReflectionUtils.getMethod(klass, getter_name);
        Object[] parameter_values;
        try {
            parameter_values = (Object[]) getter.invoke(null);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(parameter_values);
    }

    
    @Theory
    public void testRequiredArguments(@FromDataPoints("missing_args") Map args) {
        assumeFalse(isEmptyArgSet(args));
        exception.expect(MissingArgumentException.class);
        execute(args);
    }

}
