= Aerogear Push EE server tests concept

== Idea

Push EE is an application running on top of an application server. Arquillian
can deploy an app, so the remaining workload is to figure out how to replace
curl in an convenient way from Java.

=== Description of the testing approach

I tried following approaches:

* Manual using Apache HttpClient
* DSL API
* JSONObject to work with JSON
* GSON serialization
* REST-Assured framework

All are listed here: https://gist.github.com/kpiwko/5612949

In the end it seems that Groovy is the most readable while does impacting test
infrastructure heavily.

=== Why are the tests in different module than app itself?

Multiple reasons. Most importantly, the do not interfere with the app itself -
dependencies in test scope, groovy compiler, difficult profile setup.

Keeping them as a hidden module - not a module in multi-module Maven project,
allows developers to focus on development while running test is still
convenient.

== Requirements

1. Get Arquillian Spock Testrunner and install it into your local Maven repository from https://github.com/kpiwko/arquillian-testrunner-spock/tree/spock-0.7-update
2. Run mvn clean test -Parq-jbossas-managed

=== How to run against JBoss EAP 6

1. Get JBoss EAP 6.0.1 binary
2. Update with-tools version to 1.0.4.Final-redhat-wfk-1
3. Set -DjbossHome=/path/to/extracted/eap or modify arquillian.xml to point there
4. Set Maven Enterprise repository in your settings.xml (maven.repository.redhat.com/techpreview/all/)
5. Run mvn clean test -Parq-jbossas-managed
