# wf-ejbstartup-lock
Demo app for illustrating EJB startup call issue in WildFly 21+

# The Issue

This requires an EAR application that meets the following criteria:
- It has at least two `<ejb>` modules and at least one `<web>` module
- The application.xml has `<initialize-in-order>true</initialize-in-order>` and the module order is such that **the 
  initialization of the web module(s) happens after at least one EJB module and before another EJB module, which has at 
  least one singleton bean annotated with `@javax.ejb.Startup`.**
- The web module invokes an EJB in a previously initialized module during its initialization, such as from a 
  `Servlet#init()` or `ServletContextListener#contextInitialized()` method.  

In WildFly 20 and earlier, the application will deploy successfully; however, in WildFly 21+ the invocation during web module 
initialization will be stuck awaiting a condition in `org.jboss.as.ejb3.deployment.processors.StartupAwaitInterceptor.processInvocation` 
until the deployment times out after the period defined by `jboss.as.management.blocking.timeout` system property.  

In this example application, the `wf-ejbstartup-lock-commonejb.jar` module is initialized first, followed by 
`wf-ejbstartup-lock-web.war` which calls an EJB in the `wf-ejbstartup-lock-commonejb.jar` from a ServletContextListener, 
with the `wf-ejbstartup-lock-ejb2.jar` being called last.

A thread dump will show this stack trace for the EJB invocation stuck awaiting the condition:

```
"ServerService Thread Pool -- ###@#####" prio=5 tid=0x### nid=NA waiting
  java.lang.Thread.State: WAITING
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Object.wait(Object.java:328)
	  at org.jboss.as.ee.component.deployers.StartupCountdown.await(StartupCountdown.java:45)
	  at org.jboss.as.ejb3.deployment.processors.StartupAwaitInterceptor.processInvocation(StartupAwaitInterceptor.java:21)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.as.ejb3.component.interceptors.ShutDownInterceptorFactory$1.processInvocation(ShutDownInterceptorFactory.java:64)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.as.ejb3.deployment.processors.EjbSuspendInterceptor.processInvocation(EjbSuspendInterceptor.java:45)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.as.ejb3.component.interceptors.LoggingInterceptor.processInvocation(LoggingInterceptor.java:67)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.as.ee.component.NamespaceContextInterceptor.processInvocation(NamespaceContextInterceptor.java:50)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.invocation.ContextClassLoaderInterceptor.processInvocation(ContextClassLoaderInterceptor.java:60)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.invocation.InterceptorContext.run(InterceptorContext.java:438)
	  at org.wildfly.security.manager.WildFlySecurityManager.doChecked(WildFlySecurityManager.java:633)
	  at org.jboss.invocation.AccessCheckingInterceptor.processInvocation(AccessCheckingInterceptor.java:57)
	  at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
	  at org.jboss.invocation.ChainedInterceptor.processInvocation(ChainedInterceptor.java:53)
	  at org.jboss.as.ee.component.ViewService$View.invoke(ViewService.java:198)
	  at org.jboss.as.ejb3.remote.LocalEjbReceiver.processInvocation(LocalEjbReceiver.java:266)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:501)
	  at org.jboss.ejb.protocol.remote.RemotingEJBClientInterceptor.handleInvocation(RemotingEJBClientInterceptor.java:52)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:516)
	  at org.jboss.ejb.client.TransactionPostDiscoveryInterceptor.handleInvocation(TransactionPostDiscoveryInterceptor.java:82)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:516)
	  at org.jboss.ejb.client.DiscoveryEJBClientInterceptor.handleInvocation(DiscoveryEJBClientInterceptor.java:125)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:516)
	  at org.jboss.ejb.client.NamingEJBClientInterceptor.handleInvocation(NamingEJBClientInterceptor.java:69)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:516)
	  at org.jboss.ejb.client.TransactionInterceptor.handleInvocation(TransactionInterceptor.java:205)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequest(EJBClientInvocationContext.java:516)
	  at org.jboss.ejb.client.EJBClientInvocationContext$$Lambda$1359.617716583.accept(Unknown Source:-1)
	  at org.wildfly.common.context.Contextual.runExConsumer(Contextual.java:203)
	  at org.jboss.ejb.client.EJBClientInvocationContext.sendRequestInitial(EJBClientInvocationContext.java:343)
	  at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:187)
	  at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:125)
	  at com.sun.proxy.$Proxy141.listUsers(Unknown Source:-1)
	  at com.jfisherdev.wfejbstartuplock.commons.ejb.UserManagementEjbClient.listUsers(UserManagementEjbClient.java:20)
	  at com.jfisherdev.wfejbstartuplock.webapp.WebContextListener.contextInitialized(WebContextListener.java:23)
	  at io.undertow.servlet.core.ApplicationListeners.contextInitialized(ApplicationListeners.java:187)
```
