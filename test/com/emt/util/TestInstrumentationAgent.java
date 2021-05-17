package com.emt.util;

import static net.bytebuddy.matcher.ElementMatchers.declaresField;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;

import com.emt.steps.StepTestFixture;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.utility.JavaModule;

public class TestInstrumentationAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        
        new AgentBuilder.Default()
        .type(declaresField(isAnnotatedWith(
          named(Parameter.class.getName()).or(
          named(StateVar.class.getName())))))
        .transform(new AgentBuilder.Transformer() {

    @Override
    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
            JavaModule module) {
        return builder
        .defineMethod("getArgs", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
          .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
          .getDeclaredMethods()
          .filter(named("_getArgs").and(takesArguments(0)).and(isStatic()))
          .getOnly()))
        .annotateMethod(
                  AnnotationDescription.Builder.ofType(DataPoints.class)
                                               .defineArray("value", new String[] {"args"})
                                               .build())
        .defineMethod("getState", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
          .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
          .getDeclaredMethods()
          .filter(named("_getState").and(takesArguments(0)).and(isStatic()))
          .getOnly()))
        .annotateMethod(
                  AnnotationDescription.Builder.ofType(DataPoints.class)
                                               .defineArray("value", new String[] {"state"})
                                               .build())
        .defineMethod("getInputWithMissingArgs", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
          .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
          .getDeclaredMethods()
          .filter(named("_getInputWithMissingArgs").and(takesArguments(0)).and(isStatic()))
          .getOnly()))
        .annotateMethod(
                  AnnotationDescription.Builder.ofType(DataPoints.class)
                                               .defineArray("value", new String[] {"missing_args"})
                                               .build());
        }
    }).installOn(instrumentation);

    }
    
}
