package com.emt.common

import static com.emt.testing.mocks.Mocks.buildChangeLogSet
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper
import org.junit.Before
import org.junit.Test

import com.lesfurets.jenkins.unit.cps.BasePipelineTestCPS


class ChangeSetUtilsTest2 extends BasePipelineTestCPS {
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
    def change_sets = []
    change_sets.add(buildChangeLogSet(change_set))
    when(current_build.getChangeSets()).thenReturn(change_sets)
    binding.setVariable('currentBuild', current_build)

    helper.registerAllowedMethod('error', [String]) { message ->
        throw new Exception(message)
    }
  }

//  @Test
//  void getChangeLog() throws Exception {
//      helper.registerAllowedMethod('fileExists', [String]) { return true }
//
//      def change_log = new ChangeSetUtils(script).getChangeLog()
//      assertNotNull(change_log)
//      assertTrue(change_log.size() == change_set.size())
//  }
//
//  @Test(expected = Exception)
//  void getInvalidChangeLogFails() throws Exception {
//      helper.registerAllowedMethod('fileExists', [String]) { return false }
//
//      new ChangeSetUtils(script).getChangeLog()
//  }

}
