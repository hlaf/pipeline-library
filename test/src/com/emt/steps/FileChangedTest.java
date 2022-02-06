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

import org.junit.Before;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.mockito.Mockito;

import com.emt.ICurrentBuildNamespace;
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
    @StateVar boolean file_exists;

    private static String file_path_1 = "my/target/file.ext";

    public static Object[] name_values() { return new String[] { file_path_1 }; }
    
    public static Object[] change_set_values() {
        return new Object[] {
          Sets.newHashSet("a", "b", file_path_1, "c"),
          Sets.newHashSet("a", "b", "c"),
          Sets.newHashSet(),
        };
    }

    private Set<String> _c_set;

    @Before
    public void setup() {
        super.setup();
        _steps.currentBuild = mock(ICurrentBuildNamespace.class, Mockito.CALLS_REAL_METHODS);
        _steps.currentBuild.changeSets = new ArrayList<ChangeLogSet>();;
    }

    protected void commonSetup(Map args, Map state) {
        when(_steps.fileExists((String)args.get("name"))).thenReturn((boolean)state.get("file_exists"));

        _c_set = (Set<String>) state.get("change_set");
        _steps.currentBuild.changeSets.add(buildChangeLogSet(_c_set));
    }

    @Theory
    public void returnsTrueWhenFileIsInChangeLog(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue((boolean)state.get("file_exists"));
        assumeTrue(_c_set.contains(args.get("name")));
        assertTrue(execute(args).equals(true));
    }

    @Theory
    public void returnsFalseWhenFileNotInChangeLog(@FromDataPoints("args") Map args,
                                                   @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue((boolean)state.get("file_exists"));
        assumeFalse(_c_set.contains(args.get("name")));
        assertTrue(execute(args).equals(false));
    }

    @Theory
    public void returnsFalseWhenChangeLogIsEmpty(@FromDataPoints("args") Map args,
                                                 @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue((boolean)state.get("file_exists"));
        assumeTrue(_c_set.isEmpty());
        assertTrue(execute(args).equals(false));
    }

    @Theory
    public void failsWhenFileDoesNotExist(@FromDataPoints("args") Map args,
                                          @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeFalse((boolean)state.get("file_exists"));
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
        doReturn(entries.iterator()).when(change_set).iterator();

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
