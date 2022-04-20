package com.emt.common

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper
import org.junit.Before
import org.junit.Test

import com.lesfurets.jenkins.unit.BasePipelineTest

import hudson.scm.ChangeLogSet
import hudson.scm.EditType
import hudson.scm.ChangeLogSet.AffectedFile
import hudson.scm.ChangeLogSet.Entry


class ChangeSetUtilsTest extends BasePipelineTest {
  Object script
  Object change_set

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    this.script = loadScript('test/resources/EmptyPipeline.groovy')
    assertNotNull(script)
    
    this.change_set = ['a', 'bar', 'foo']

    def current_build = mock(RunWrapper.class)
    List<ChangeLogSet<? extends Entry>> change_sets = new ArrayList<ChangeLogSet<? extends ChangeLogSet.Entry>>();
    change_sets.add(buildChangeLogSet(change_set))
    when(current_build.getChangeSets()).thenReturn(change_sets)
    binding.setVariable('currentBuild', current_build)

    helper.registerAllowedMethod('error', [String]) { message ->
        throw new Exception(message)
    }
  }

  @Test
  void getChangeLog() throws Exception {
      helper.registerAllowedMethod('fileExists', [String]) { return true }

      def change_log = new ChangeSetUtils(script).getChangeLog()
      assertNotNull(change_log)
      assertTrue(change_log.size() == change_set.size())
  }

  @Test(expected = Exception)
  void getInvalidChangeLogFails() throws Exception {
      helper.registerAllowedMethod('fileExists', [String]) { return false }

      new ChangeSetUtils(script).getChangeLog()
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
