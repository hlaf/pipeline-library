package com.emt.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.emt.util.Parameter;

@RunWith(Theories.class)
public class CheckoutFromBzrTest extends StepTestFixture {

    @Parameter(values = { "my_url" })
    String repo_url;

    @Theory
    public void callsBzr(@FromDataPoints("args") Map args) {
        assumeTrue(true);
        execute(args);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(_steps).sh(captor.capture());
        assertTrue(captor.getValue().contains("bzr"));
    }

}
