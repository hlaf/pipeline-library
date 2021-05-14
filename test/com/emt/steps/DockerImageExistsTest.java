package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;
import com.emt.util.StateVar;

public class DockerImageExistsTest extends StepTestFixture {

    @Parameter String image_name;
    @StateVar boolean image_exists;

    @Theory
    public void callsShWithCommand(@FromDataPoints("args") Map args, @FromDataPoints("state") Map state) {

        when(_steps.sh(anyMap())).thenReturn((boolean)state.get("image_exists") ? 0 : 1);

        boolean res = (boolean) inst().execute(args);
        verify(_steps).sh(any(Map.class));
        assert res == (boolean) state.get("image_exists");
    }

}
