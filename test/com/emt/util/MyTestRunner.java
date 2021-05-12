package com.emt.util;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.experimental.theories.Theories;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.emt.steps.StepTestFixture;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodCall;

public class MyTestRunner extends Theories {

    public MyTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass
            /*new ByteBuddy()
                .rebase(testClass)
                .defineMethod("getDumboArgs", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
                .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
                .getDeclaredMethods()
                .filter(named("_getArgs").and(takesArguments(0)).and(isStatic()))
                .getOnly()))
                .make()
                .load(testClass.getClassLoader()).getClass()*/);

    }
    
    private void enhanceTestClass() {
        ByteBuddyAgent.install();
        new ByteBuddy()
        .rebase(this.getTestClass().getJavaClass())
        .defineMethod("getDumboArgs", Map[].class, Modifier.PUBLIC | Modifier.STATIC)
        .intercept(MethodCall.invoke(new TypeDescription.ForLoadedType(StepTestFixture.class)
        .getDeclaredMethods()
        .filter(named("_getArgs").and(takesArguments(0)).and(isStatic()))
        .getOnly()))
        .make()
        .load(this.getTestClass().getJavaClass().getClassLoader(),
              ClassReloadingStrategy.fromInstalledAgent());
    }
    
    // Runs junit tests in a separate thread using the custom class loader
    @Override
    public void run(final RunNotifier notifier) {
      Runnable runnable = () -> {
        super.run(notifier);
      };
      Thread thread = new Thread(runnable);
      enhanceTestClass();
      thread.setContextClassLoader(this.getTestClass().getJavaClass().getClassLoader());
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    

}
