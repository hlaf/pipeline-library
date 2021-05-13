package com.emt.util;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;

import com.emt.steps.StepTestFixture;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.utility.JavaModule;


public class MyAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("This is the Java Agent!");
        
        new AgentBuilder.Default()
        .type(nameStartsWith("com.emt.steps.CheckoutFromBzrTest").or(
              nameStartsWith("com.emt.steps.BuildDockerImageTest")))
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
                                               .build());
    }
    }).installOn(inst);

    }
    
    public static Class<?> enhanceTestClassAtRuntime(Class<?> clazz) {
        Class<?> clazz_enhanced = new ByteBuddy()
        .redefine(clazz)
        .defineMethod("getArgs", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
          .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
          .getDeclaredMethods()
          .filter(named("_getArgs").and(takesArguments(0)).and(isStatic()))
          .getOnly()))
        .annotateMethod(
                  AnnotationDescription.Builder.ofType(DataPoints.class)
                                               .defineArray("value", new String[] {"args"})
                                               .build())
        .make()
        .load(ClassLoader.getSystemClassLoader(),
              ClassLoadingStrategy.Default.CHILD_FIRST)
        .getLoaded();
        return clazz_enhanced;
    }
    
}
