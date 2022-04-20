package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.IChangeSetUtils;
import com.emt.util.Parameter;
import com.emt.util.StateVar;

public class FileChangedTest extends StepTestFixture {

    @Parameter String name;
    @StateVar Set<String> change_set;
    @StateVar boolean input_file_exists;
    @StateVar boolean other_files_exist;

    private static String file_path_1 = "my/target/file.ext";

    public static Object[] name_values() { [file_path_1].toArray() }
    
    public static Object[] change_set_values() {
        return [
          ["a", "b", file_path_1, "c"],
          ["a", "b", "c"],
          [],
          [file_path_1],
        ].collect { it.toSet() }.toArray()
    }

    private Set<String> _c_set;

    protected void commonSetup(Map args, Map state) {
        _steps.changeSetUtils = mock(IChangeSetUtils.class);

        _c_set = state['change_set']

        when(_steps.fileExists(args['name'])).thenReturn(state['input_file_exists'])
        for (String file_path: _c_set) {
            if (file_path != args['name']) {
                when(_steps.fileExists(file_path)).thenReturn(state['other_files_exist'])
            }
        }

        try {
            when(_steps.changeSetUtils.getChangeLog()).thenReturn(_c_set.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
    private static boolean implies(boolean a, boolean b) {
        return (!a) || b;
    }

    // Predicate
    boolean changeLogIsValid(Map args, Map state) {
        return (changeLogIsEmpty(args, state)
                ||
                (
                 implies(changeLogContainsInputFile(args, state),
                         state['input_file_exists']) &&
                 implies(changeLogContainsOtherFiles(args, state),
                         state['other_files_exist'])
                )
        );               
    }

    boolean changeLogContainsInputFile(Map args, Map state) {
        return _c_set.contains(args['name'])
    }

    boolean changeLogContainsOtherFiles(Map args, Map state) {
        return !changeLogIsEmpty(args, state) &&
               implies(changeLogContainsInputFile(args, state),
                       !changeLogIsSingleton(args, state));
    }

    boolean changeLogIsSingleton(Map args, Map state) {
        return _c_set.size() == 1;
    }

    boolean changeLogIsEmpty(Map args, Map state) {
        return _c_set.isEmpty();
    }

    @Theory
    public void returnsTrueWhenFileIsInChangeLog(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        commonSetup(args, state)
        assumeTrue(state['input_file_exists'])
        assumeTrue(state['other_files_exist'])
        assumeTrue(changeLogContainsInputFile(args, state))
        boolean res = execute(args)
        assertTrue(res)
    }

    @Theory
    public void returnsFalseWhenFileNotInChangeLog(@FromDataPoints("args") Map args,
                                                   @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue(state['input_file_exists'])
        assumeTrue(state['other_files_exist'])
        assumeFalse(changeLogContainsInputFile(args, state))
        assertTrue(execute(args).equals(false))
    }

    @Theory
    public void returnsFalseWhenChangeLogIsEmpty(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue(state['input_file_exists'])
        assumeTrue(_c_set.isEmpty())
        assertTrue(execute(args).equals(false))
    }

    @Theory
    public void failsWhenFileDoesNotExist(@FromDataPoints("args") Map args,
                                          @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeFalse(state['input_file_exists'])
        execute(args)
        assertTrue(error_was_called());
    }

//    @Theory
//    public void failsWhenTheChangeLogIsNotValid(@FromDataPoints("args") Map args,
//                                                @FromDataPoints("state") Map state) {
//        commonSetup(args, state);
//        assumeFalse(changeLogIsValid(args, state));
//        execute(args);
//
//        assertTrue(error_was_called());
//    }
}
