package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;
import com.emt.util.StateVar;
import com.google.common.collect.Sets;

import hudson.scm.ChangeLogSet;
import hudson.scm.EditType;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

public class FileChangedTest extends StepTestFixture {

    @Parameter String name;
    @StateVar Set<String> change_set;
    @StateVar boolean input_file_exists;
    @StateVar boolean other_files_exist;

    private static String file_path_1 = "my/target/file.ext";

    public static Object[] name_values() { return new String[] { file_path_1 }; }
    
    public static Object[] change_set_values() {
        return new Object[] {
          Sets.newHashSet("a", "b", file_path_1, "c"),
          Sets.newHashSet("a", "b", "c"),
          Sets.newHashSet(),
          Sets.newHashSet(file_path_1),
        };
    }

    private Set<String> _c_set;

    protected void commonSetup(Map args, Map state) {
        _c_set = (Set<String>) state.get("change_set");

        when(_steps.fileExists((String)args.get("name")))
          .thenReturn((boolean)state.get("input_file_exists"));
        for (String file_path: _c_set) {
            if (file_path != (String)args.get("name")) {
                when(_steps.fileExists(file_path)).thenReturn((boolean)state.get("other_files_exist"));
            }
        }

        List<ChangeLogSet<? extends Entry>> change_sets = new ArrayList<ChangeLogSet<? extends ChangeLogSet.Entry>>();
        change_sets.add(buildChangeLogSet(_c_set));

        try {
            when(_steps.currentBuild.getChangeSets())
             .thenReturn(change_sets);
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
                        (boolean) state.get("input_file_exists")) &&
                 implies(changeLogContainsOtherFiles(args, state),
                        (boolean) state.get("other_files_exist"))
                )
        );               
    }

    boolean changeLogContainsInputFile(Map args, Map state) {
        return _c_set.contains(args.get("name"));
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
        commonSetup(args, state);
        assumeTrue((boolean)state.get("input_file_exists"));
        assumeTrue((boolean)state.get("other_files_exist"));
        assumeTrue(changeLogContainsInputFile(args, state));
        boolean res = (boolean) execute(args);
        assertTrue(res);
    }

    @Theory
    public void returnsFalseWhenFileNotInChangeLog(@FromDataPoints("args") Map args,
                                                   @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue((boolean)state.get("input_file_exists"));
        assumeTrue((boolean)state.get("other_files_exist"));
        assumeFalse(changeLogContainsInputFile(args, state));
        assertTrue(execute(args).equals(false));
    }

    @Theory
    public void returnsFalseWhenChangeLogIsEmpty(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue((boolean)state.get("input_file_exists"));
        assumeTrue(_c_set.isEmpty());
        assertTrue(execute(args).equals(false));
    }

    @Theory
    public void failsWhenFileDoesNotExist(@FromDataPoints("args") Map args,
                                          @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeFalse((boolean)state.get("input_file_exists"));
        execute(args);

        assertTrue(error_was_called());
    }
    
    @Theory
    public void failsWhenTheChangeLogIsNotValid(@FromDataPoints("args") Map args,
                                                @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeFalse(changeLogIsValid(args, state));
        execute(args);

        assertTrue(error_was_called());
    }

    private static ChangeLogSet buildChangeLogSet(Collection<String> file_paths) {
        ChangeLogSet change_set = mock(ChangeLogSet.class);

        List<Entry> entries = new ArrayList<Entry>();
        ChangeLogSet.Entry entry = mock(ChangeLogSet.Entry.class);
        entries.add(entry);

        List<AffectedFile> affected_files = new ArrayList<AffectedFile>();
        for (String file_path : file_paths) {
            AffectedFileMock f = new AffectedFileMock(file_path, EditType.EDIT);
            affected_files.add(f);
        }

        doReturn(affected_files).when(entry).getAffectedFiles();
        when(change_set.iterator()).
          thenReturn(entries.iterator()).
          thenReturn(entries.iterator());

        return change_set;
    }

}

class AffectedFileMock implements ChangeLogSet.AffectedFile {

    private final String _path;
    private final EditType _edit_type;

    AffectedFileMock(String path, EditType edit_type) {
        _path = path;
        _edit_type = edit_type;
    }

    @Override
    public String getPath() {
        return _path;
    }

    @Override
    public EditType getEditType() {
        return _edit_type;
    }

}
