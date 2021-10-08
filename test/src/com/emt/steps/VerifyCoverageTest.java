package com.emt.steps;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;

import com.emt.ICurrentBuildNamespace;
import com.emt.util.Parameter;
import com.emt.util.StateVar;


public class VerifyCoverageTest extends StepTestFixture {

	@Parameter(values={"my_key"}, optional=true) String key;

	@StateVar Map PreviousBuild;
	@StateVar(values = {"0.1", "1.0"}) double PreviousBranchRate;
	@StateVar(values = {"0.1", "1.0"}) double PreviousLineRate;
	@StateVar(values = {"0.1", "1.0"}) double BranchRate;
	@StateVar(values = {"0.1", "1.0"}) double LineRate;
	
    public static Object[] PreviousBuild_values() {
    	Map previous_build1 = new HashMap();
		previous_build1.put("number", "123");
        return new Object[]{ previous_build1, new NullValue() };
    }
    
	@Before
    public void setup() {
    	super.setup();
    	_steps.currentBuild = mock(ICurrentBuildNamespace.class);
    }

	private void commonSetup(Map args, Map state) {
		_args = args;
		_state = state;
	}
	
	@Override
	public void postInst(BaseStep inst) {
		Object previous_build = _state.get("PreviousBuild") instanceof NullValue ? null : _state.get("PreviousBuild");
		doReturn(previous_build).when((VerifyCoverage)inst).getLatestSuccessfulBuildWithArtifacts(any());
		
		doReturn(Arrays.asList(_state.get("PreviousBranchRate"), _state.get("PreviousLineRate"))).when(
				(VerifyCoverage)inst).parseCoverageXml(any(), matches(".*previous_build.*"));

		doReturn(Arrays.asList(_state.get("BranchRate"), _state.get("LineRate"))).when(
				(VerifyCoverage)inst).parseCoverageXml(any(), AdditionalMatchers.not(matches(".*previous_build.*")));
	}
	
	private boolean previousBuildExists(Map state) {
		return state.get("PreviousBuild") instanceof NullValue == false;
	}
	
	private boolean coverageDecreased(Map state) {
		if (previousBuildExists(state)) {
			return ((double)state.get("BranchRate") < (double)state.get("PreviousBranchRate") ||
			        (double)state.get("LineRate") < (double)state.get("PreviousLineRate"));
		} else {
			return ((double)state.get("BranchRate") < VerifyCoverage.DEFAULT_BASELINE ||
			        (double)state.get("LineRate") < VerifyCoverage.DEFAULT_BASELINE);
		}
	}
	
	@Theory
    public void failsWhenCoverageBelowPreviousBuild(@FromDataPoints("args") Map args,
    		                                        @FromDataPoints("state") Map state) {
		assumeTrue(previousBuildExists(state) && coverageDecreased(state));
    	commonSetup(args, state);

    	exception.expect(RuntimeException.class);
    	execute(args);
    	
    	verify(_steps).error(anyString());
    }

	@Theory
	public void succeedsWhenCoverageNotBelowPreviousBuild(@FromDataPoints("args") Map args,
                                                          @FromDataPoints("state") Map state) {
		assumeTrue(previousBuildExists(state) && !coverageDecreased(state));
		commonSetup(args, state);
		
		execute(args);
	}
	
	@Theory
	public void succeedsWhenNotBelowDefaultBaseline(@FromDataPoints("args") Map args,
                                                    @FromDataPoints("state") Map state) {
		assumeTrue(!previousBuildExists(state) && !coverageDecreased(state));
		commonSetup(args, state);
		
		execute(args);
	}
	
	@Theory
	public void failsWhenBelowDefaultBaseline(@FromDataPoints("args") Map args,
                                              @FromDataPoints("state") Map state) {
		assumeTrue(!previousBuildExists(state) && coverageDecreased(state));
		commonSetup(args, state);
		exception.expect(RuntimeException.class);
		
		execute(args);
		
		verify(_steps).error(anyString());
	}
}
