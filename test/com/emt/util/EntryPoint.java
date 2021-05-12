package com.emt.util;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.experimental.theories.DataPoints;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.emt.steps.StepTestFixture;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.pool.TypePool;

public class EntryPoint {

    private static Class<?> enhanceTestClass(String class_name) {
        TypePool typePool = TypePool.Default.ofSystemLoader();
        
        Class<?> clazz = new ByteBuddy()
        //  .subclass(CheckoutFromBzrTest.class)
        .redefine(typePool.describe(class_name).resolve(),
                  ClassFileLocator.ForClassLoader.ofSystemLoader())
        .defineMethod("getArgs",
                      Map[].class,
                      Modifier.PUBLIC | Modifier.STATIC)
          .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
          .getDeclaredMethods()
          .filter(named("_getArgs").and(takesArguments(0)).and(isStatic()))
          .getOnly()))
          .annotateMethod(
            AnnotationDescription.Builder.ofType(DataPoints.class)
                                         .defineArray("value", new String[] {"args"})
                                         .build())
        .make()
        .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
        .getLoaded();

        return clazz;
    }

    public static void main(String[] args) {
        Class<?> clazz = enhanceTestClass("com.emt.steps.CheckoutFromBzrTest");

        Result result = JUnitCore.runClasses(clazz);
        for (Failure failure: result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

}
