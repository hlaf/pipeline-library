package com.emt.steps;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class UnstashCoverageResultTest extends StepTestFixture {

    @Parameter(values={"my_results_key"}) String key;

    @Theory
    public void returnsPathOfUnstashedResults(@FromDataPoints("args") Map args) {
        String result = (String) execute(args);

        assert result.endsWith((String)args.get("key"));
        assert result.contains(UnstashCoverageResult.COVERAGE_RESULTS_BASE_DIR);
    }

}
