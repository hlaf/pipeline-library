package com.legrig

import com.cloudbees.groovy.cps.impl.CpsCallableInvocation

import spock.lang.IgnoreIf
import spock.lang.Unroll
import support.jenkins.CPSSpecification

class MyGroovyClassSpec extends CPSSpecification {

  Class testSubjectClass = MyGroovyClass

  @Unroll
  def "MyGroovyClass.nonCPSMultiply(#x, #y) == #expected Test"() {

    expect: 'nonCPSMultiply to work without CPS'
      sut.nonCPSMultiply(x, y) == expected

    where: 'data is'
      x | y || expected
      1 | 1 || 1
      1 | 0 || 0
      2 | 3 || 6
  }

  @IgnoreIf({ Boolean.getBoolean('com.emt.use_cps') == false })
  def "MyGroovyClass.multiply() will not run without CPS Test"() {
    when: 'Data to be correct'
      sut.multiply(1, 2)

    then: 'CpsCallableInvocation Exception is thrown'
      thrown(CpsCallableInvocation)
  }

  @IgnoreIf({ Boolean.getBoolean('com.emt.use_cps') == false })
  @Unroll
  def "MyGroovyClass.multiply(#x, #y)== #expected under CPS Test"() {

    when: 'we invoke multiply() via CPS'
      def result = invokeCPSMethod(sut, 'multiply', x, y)

    then: 'Exception is not thrown'
      notThrown(CpsCallableInvocation)

    and: 'result is correct'
      result == expected

    where: 'data is'
      x | y || expected
      1 | 1 || 1
      1 | 0 || 0
      2 | 3 || 6
  }
}
