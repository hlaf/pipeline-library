package com.emt.testing.mocks;

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import hudson.scm.ChangeLogSet
import hudson.scm.EditType
import hudson.scm.ChangeLogSet.AffectedFile
import hudson.scm.ChangeLogSet.Entry

public static ChangeLogSet buildChangeLogSet(Collection<String> file_paths) {
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
