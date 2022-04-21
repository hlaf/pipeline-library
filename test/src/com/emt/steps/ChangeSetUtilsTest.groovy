package com.emt.steps;

import static com.emt.testing.mocks.Mocks.buildChangeLogSet
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assume.assumeFalse
import static org.junit.Assume.assumeTrue
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import org.junit.experimental.theories.FromDataPoints
import org.junit.experimental.theories.Theory
import org.mockito.ArgumentCaptor

import com.emt.common.ChangeSetUtils
import com.emt.util.StateVar

import support.cps.CPSUtils

public class ChangeSetUtilsTest extends StepTestFixture {

    @StateVar Set<String> change_set
    @StateVar boolean has_unmapped_files

    public static Object[] change_set_values() {
        return [
          ["a", "b", "c"],
          [],
          ["a"],
        ].collect { it.toSet() }.toArray()
    }

    protected void commonSetup(Map args, Map state) {
        for (String file_path: state['change_set']) {
            when(_steps.fileExists(file_path)).thenReturn(!state['has_unmapped_files'])
        }

        def change_sets = [buildChangeLogSet(state['change_set'])]
        when(_steps.currentBuild.getChangeSets()).thenReturn(change_sets)
    }

    @Theory
    public void retrievesChangeLog(@FromDataPoints("args") Map args,
                                   @FromDataPoints("state") Map state) {
        assumeFalse(state['has_unmapped_files'])

        commonSetup(args, state)
        def change_log = invokeLibFunction(new ChangeSetUtils(_steps), 'getChangeLog')

        assertNotNull(change_log)
        assertTrue(change_log.size() == state['change_set'].size())
    }
    
    @Theory
    public void failsWhenChangeLogIsNotValid(@FromDataPoints("args") Map args,
                                             @FromDataPoints("state") Map state) {
        assumeTrue(state['has_unmapped_files'] && !state['change_set'].isEmpty())

        commonSetup(args, state)

        invokeLibFunction(new ChangeSetUtils(_steps), 'getChangeLog')
        def captor = ArgumentCaptor.forClass(String.class)
        verify(_steps, times(1)).error(captor.capture())
        assertTrue(captor.getValue().contains('unmapped files'))
    }

}
