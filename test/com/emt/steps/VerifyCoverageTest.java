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
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;

import com.emt.ICurrentBuildNamespace;

@RunWith(Theories.class)
public class VerifyCoverageTest extends StepTestFixture {

	public static List<String> getParameters() {
		return Arrays.asList("key");
	}

	public static List<String> getStateVariables() {
		return Arrays.asList("PreviousBuild",
				             "PreviousBranchRate",
				             "PreviousLineRate",
				             "BranchRate",
				             "LineRate");
	}

    public static Object[] key_values() {
        return new Object[]{ "my_key", new Unassigned() };
    }
	
    public static Object[] PreviousBuild_values() {
    	Map previous_build1 = new HashMap();
		previous_build1.put("number", "123");
        return new Object[]{ previous_build1, new NullValue() };
    }
    
    public static Object[] PreviousBranchRate_values() {
        return new Object[]{ 0.1, 1.0 };
    }

    public static Object[] PreviousLineRate_values() {
        return new Object[]{ 0.1, 1.0 };
    }
    
    public static Object[] BranchRate_values() {
    	return new Object[]{ 0.1, 1.0 };
    }

    public static Object[] LineRate_values() {
    	return new Object[]{ 0.1, 1.0 };
    }
    
	@DataPoints("args") public static Map[] getArgs() { return _getArgs(); }

	@DataPoints("state") public static Map[] getState() { return _getState(); }
	
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
    	inst().execute(args);
    	
    	verify(_steps).error(anyString());
    }

	@Theory
	public void succeedsWhenCoverageNotBelowPreviousBuild(@FromDataPoints("args") Map args,
                                                          @FromDataPoints("state") Map state) {
		assumeTrue(previousBuildExists(state) && !coverageDecreased(state));
		commonSetup(args, state);
		
		inst().execute(args);
	}
	
	@Theory
	public void succeedsWhenNotBelowDefaultBaseline(@FromDataPoints("args") Map args,
                                                    @FromDataPoints("state") Map state) {
		assumeTrue(!previousBuildExists(state) && !coverageDecreased(state));
		commonSetup(args, state);
		
		inst().execute(args);
	}
	
	@Theory
	public void failsWhenBelowDefaultBaseline(@FromDataPoints("args") Map args,
                                              @FromDataPoints("state") Map state) {
		assumeTrue(!previousBuildExists(state) && coverageDecreased(state));
		commonSetup(args, state);
		exception.expect(RuntimeException.class);
		
		inst().execute(args);
		
		verify(_steps).error(anyString());
	}
}
