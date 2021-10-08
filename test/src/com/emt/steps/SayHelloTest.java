package com.emt.steps;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;

public class SayHelloTest extends StepTestFixture {

    @Parameter(values={"World"}, optional=true) String name;

    @Theory
    public void callsEchoWithDefaultMessage(@FromDataPoints("args") Map args) {
        assumeFalse(args.containsKey("name"));
        execute(args);
        verify(_steps).echo("Hello, human.");
    }

    @Theory
    public void callsEchoWithCustomMessage(@FromDataPoints("args") Map args) {
        assumeTrue(args.containsKey("name"));
        execute(args);
        verify(_steps).echo(String.format("Hello, %s.", args.get("name")));
    }

}
