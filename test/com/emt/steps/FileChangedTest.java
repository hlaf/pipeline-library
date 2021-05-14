package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emt.ICurrentBuildNamespace;
import com.emt.util.Parameter;
import com.emt.util.StateVar;

import hudson.scm.ChangeLogSet;
import hudson.scm.EditType;
import hudson.scm.ChangeLogSet.AffectedFile;

public class FileChangedTest extends StepTestFixture {

    @Parameter String name;

    @StateVar ChangeLogSet change_set;

    private List<ChangeLogSet> _change_sets;

    @Before
    public void setup() {
        super.setup();
        _steps.currentBuild = mock(ICurrentBuildNamespace.class, Mockito.CALLS_REAL_METHODS);
        _change_sets = new ArrayList<ChangeLogSet>();
        _steps.currentBuild.changeSets = _change_sets;
    }

    private ChangeLogSet buildChangeLogSet(List<String> file_paths) {
        ChangeLogSet change_set = mock(ChangeLogSet.class);

        List entries = new ArrayList();
        ChangeLogSet.Entry entry = mock(ChangeLogSet.Entry.class);
        entries.add(entry);

        List<AffectedFile> affected_files = new ArrayList();
        for (String file_path : file_paths) {
            AffectedFileMock f = new AffectedFileMock(file_path, EditType.EDIT);
            affected_files.add(f);
        }

        doReturn(affected_files).when(entry).getAffectedFiles();
        doReturn(entries.iterator()).when(change_set).iterator();

        _change_sets.add(change_set);
        return change_set;
    }

    @Test
    public void returnsTrueWhenFileIsInChangeLog() {
        Map args = new HashMap();
        String file_path = "my/target/file.ext";
        args.put("name", file_path);

        buildChangeLogSet(Arrays.asList("a", "b", file_path, "c"));

        assertTrue(inst().execute(args).equals(true));
    }

    @Test
    public void returnsFalseWhenFileNotInChangeLog() {
        Map args = new HashMap();
        String file_path = "my/target/file.ext";
        args.put("name", file_path);

        buildChangeLogSet(Arrays.asList("a", "b", "c"));

        assertTrue(inst().execute(args).equals(false));
    }

    @Test
    public void returnsFalseWhenChangeLogIsEmpty() {
        Map args = new HashMap();
        String file_path = "my/target/file.ext";
        args.put("name", file_path);

        buildChangeLogSet(Arrays.asList());

        assertTrue(inst().execute(args).equals(false));
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
