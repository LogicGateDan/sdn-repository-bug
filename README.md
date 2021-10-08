# SDN Super-Class Domain Entity Repository Behavior

## Overview

### Versions

| Resources    | Version |
| ------------ | ------- |
| Neo4j        | 4.3.3   |
| Neo4j Driver | 4.2.7   |
| Spring Boot  | 2.5.5   |
| SDN          | 6.1.5   |


### Description

This simple example demonstrates what appears to be unexpected behavior in SDN when attempting to use a repository
based on an abstract super-class domain entity to query results that should return objects of descendent domain class 
types.

The project has three domain classes; one abstract super-class, `Person`, and two descendents, `EvilPerson` 
and `GoodPerson`. The classes are organized as follows:

- Person
  - GoodPerson
  - EvilPerson

Rather than creating a repository interface for each of the descendent domain entities, it would be desirable to create
a single repository for the parent class. Although this is a contrived example, the value of setting things up this way
would be readily apparent in scenarios where there were a large number of descendent class types and the majority of
queries would be across all of those types instead of across a specific sub-type.

## Setup

To run the tests, you'll need a local Neo4j instance available, and you'll need to configure the application to properly
connect to the database. The `application.yml` file is set up to populate the database configuration values from
environment variables, but you can also edit the `application.yml` file directly if that seems easier.

If you choose to use environment variables, the following list shows what this might look like in an `.env` file. An
`.env.default` is available to copy to make this set up as straightforward as possible.

- NEO4J_DATABASE=neo4j
- NEO4J_PASSWORD=<some password>
- NEO4J_USERNAME=neo4j
- NEO4J_URL=bolt://localhost:7687

## Run

Once your database configuration is complete you can run:

`./gradlew cleanTest test`

## Test Cases

There are three tests that show different behavior depending on which repositories are created and pulled into the 
application context, and depending on how the data is persisted to the database.

### PersonRepositoryFailureTest

In this test case, only `PersonRepository` has been added to the application context. There are no repositories for the
descendent domain classes. The test data is persisted via the `Neo4jClient` rather than using repository save methods.
When executing `PersonRepository: Collection<Person> findPeopleByIdsIn(Collection<String> ids);` across a data set that 
contains nodes of descendent class types `GoodPerson` and `EvilPerson`, an exception is thrown:

```
org.springframework.data.mapping.MappingException: Error mapping Record<{person: node<4>}>
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.read(DefaultNeo4jEntityConverter.java:115)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.read(DefaultNeo4jEntityConverter.java:70)
	at app//org.springframework.data.neo4j.core.mapping.Schema.lambda$getRequiredMappingFunctionFor$0(Schema.java:96)
	at app//org.springframework.data.neo4j.core.PreparedQuery$AggregatingMappingFunction.apply(PreparedQuery.java:234)
	at app//org.springframework.data.neo4j.core.PreparedQuery$AggregatingMappingFunction.apply(PreparedQuery.java:154)
	at app//org.springframework.data.neo4j.core.DelegatingMappingFunctionWithNullCheck.apply(DelegatingMappingFunctionWithNullCheck.java:45)
	at app//org.springframework.data.neo4j.core.DelegatingMappingFunctionWithNullCheck.apply(DelegatingMappingFunctionWithNullCheck.java:35)
	at app//org.springframework.data.neo4j.core.DefaultNeo4jClient$DefaultRecordFetchSpec.lambda$partialMappingFunction$0(DefaultNeo4jClient.java:391)
	at java.base@11.0.11/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
	at java.base@11.0.11/java.util.Iterator.forEachRemaining(Iterator.java:133)
	at java.base@11.0.11/java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)
	at java.base@11.0.11/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
	at java.base@11.0.11/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
	at java.base@11.0.11/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
	at java.base@11.0.11/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base@11.0.11/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
	at app//org.springframework.data.neo4j.core.DefaultNeo4jClient$DefaultRecordFetchSpec.all(DefaultNeo4jClient.java:378)
	at java.base@11.0.11/java.util.Optional.map(Optional.java:265)
	at app//org.springframework.data.neo4j.core.Neo4jTemplate$DefaultExecutableQuery.getResults(Neo4jTemplate.java:947)
	at app//org.springframework.data.neo4j.repository.query.Neo4jQueryExecution$DefaultQueryExecution.execute(Neo4jQueryExecution.java:51)
	at app//org.springframework.data.neo4j.repository.query.AbstractNeo4jQuery.execute(AbstractNeo4jQuery.java:94)
	at app//org.springframework.data.repository.core.support.RepositoryMethodInvoker.doInvoke(RepositoryMethodInvoker.java:137)
	at app//org.springframework.data.repository.core.support.RepositoryMethodInvoker.invoke(RepositoryMethodInvoker.java:121)
	at app//org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.doInvoke(QueryExecutorMethodInterceptor.java:159)
	at app//org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.invoke(QueryExecutorMethodInterceptor.java:138)
	at app//org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at app//org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.invoke(DefaultMethodInvokingMethodInterceptor.java:80)
	at app//org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at app//org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)
	at app//org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)
	at app//org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
	at app//org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at app//org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:137)
	at app//org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at app//org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)
	at app//org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at app//org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:215)
	at app//com.sun.proxy.$Proxy68.findPeopleByIdsIn(Unknown Source)
	at app//com.logicgate.sample.PersonRepositoryFailureTest.findPeopleByIdsIn(PersonRepositoryFailureTest.java:64)
	at java.base@11.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base@11.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base@11.0.11/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base@11.0.11/java.lang.reflect.Method.invoke(Method.java:566)
	at app//org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:688)
	at app//org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
	at app//org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
	at app//org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:149)
	at app//org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:140)
	at app//org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:84)
	at app//org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)
	at app//org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)
	at app//org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
	at app//org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
	at app//org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
	at app//org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
	at app//org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)
	at app//org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)
	at app//org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:210)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:206)
	at app//org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:131)
	at app//org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:65)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
	at app//org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
	at java.base@11.0.11/java.util.ArrayList.forEach(ArrayList.java:1541)
	at app//org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
	at app//org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
	at java.base@11.0.11/java.util.ArrayList.forEach(ArrayList.java:1541)
	at app//org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
	at app//org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
	at app//org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
	at app//org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
	at app//org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:32)
	at app//org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
	at app//org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:51)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:108)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:88)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:54)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:67)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:52)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:96)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:75)
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.processAllTestClasses(JUnitPlatformTestClassProcessor.java:99)
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.access$000(JUnitPlatformTestClassProcessor.java:79)
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor.stop(JUnitPlatformTestClassProcessor.java:75)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.stop(SuiteTestClassProcessor.java:61)
	at java.base@11.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base@11.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base@11.0.11/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base@11.0.11/java.lang.reflect.Method.invoke(Method.java:566)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at com.sun.proxy.$Proxy2.stop(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker$3.run(TestWorker.java:193)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.executeAndMaintainThreadName(TestWorker.java:129)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:100)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:60)
	at org.gradle.process.internal.worker.child.ActionExecutionWorker.execute(ActionExecutionWorker.java:56)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:133)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:71)
	at app//worker.org.gradle.process.internal.worker.GradleWorkerMain.run(GradleWorkerMain.java:69)
	at app//worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)
Caused by: org.springframework.data.mapping.model.MappingInstantiationException: Failed to instantiate com.logicgate.sample.domain.Person using constructor public com.logicgate.sample.domain.Person(java.lang.String) with arguments p-1
	at app//org.springframework.data.mapping.model.ClassGeneratingEntityInstantiator$MappingInstantiationExceptionEntityInstantiator.createInstance(ClassGeneratingEntityInstantiator.java:321)
	at app//org.springframework.data.mapping.model.ClassGeneratingEntityInstantiator.createInstance(ClassGeneratingEntityInstantiator.java:89)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.instantiate(DefaultNeo4jEntityConverter.java:431)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.lambda$map$3(DefaultNeo4jEntityConverter.java:277)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.map(DefaultNeo4jEntityConverter.java:294)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.map(DefaultNeo4jEntityConverter.java:251)
	at app//org.springframework.data.neo4j.core.mapping.DefaultNeo4jEntityConverter.read(DefaultNeo4jEntityConverter.java:113)
	... 121 more
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.logicgate.sample.domain.Person]: Class is abstract
	... 128 more
```

### PersonRepositorySuccessWithDummyReposTest

In this test case, the repositories for the descendent classes, `GoodPersonRepository` and `EvilPersonRepository`, have 
been added to the application context (in addition to the `PersonRepository`). The test data is persisted via the 
`Neo4jClient` rather than using repository save methods. When executing 
`PersonRepository: Collection<Person> findPeopleByIdsIn(Collection<String> ids);` across a data set that contains nodes 
of descendent class types `GoodPerson` and `EvilPerson` no exception is thrown and the proper results are returned.

While this works, it is cumbersome to have to create "empty" repositories for each of the sub-types when they aren't
actually being used. In this contrived the overhead is low, but in a project with a large number of descendent domain
entities the bloat would be more material.

### PersonRepositorySuccessWithSavingTest

In this test case, only `PersonRepository` has been added to the application context. There are no repositories for the
descendent domain classes. The test data is persisted via the `PersonRepository`, as opposed to using the `Neo4jClient` 
like the other tests. When executing `PersonRepository: Collection<Person> findPeopleByIdsIn(Collection<String> ids);` 
across a data set that contains nodes of descendent class types `GoodPerson` and `EvilPerson` no exception is thrown 
and the proper results are returned.

While this works in this contrived example, it's not a viable solution for a real application. It is often necessary
for applications to interact with pre-existing data sets, so the project needs to be able to properly map database 
results to entity classes even when the data wasn't persisted with a Spring repository.