# Aurora Spring Boot Base Starter

The base starter provides the basic setup required for Spring Boot applications.
It has the following main dependencies:
- Spring Boot
- Spring Actuator
- Spring Sleuth (ready for Kafka -> Zipkin)
- Micrometer / Prometheus

## How to use
Include the starter as a dependency

```xml
<dependency>
  <groupId>no.skatteetaten.aurora.springboot</groupId>
  <artifactId>aurora-spring-boot-base-starter</artifactId>
  <version>${aurora.starters.version}</version>
</dependency>
```

In order to deploy this application on the [AuroraPlatform](https://skatteetaten.github.io/aurora) using [AuroraConfig](https://skatteetaten.github.io/aurora/documentation/aurora-config/) the following must be specified in the base file:

```yaml
actuator:
  path: /management/prometheus
```

## Metrics

The starter will automatically add [Micrometer](http://micrometer.io) as the default
metrics registry and also add `micrometer-registry-prometheus` for providing the `/prometheus` actuator endpoint that
will be (optionally) read by Prometheus automatically for all applications deployed on Aurora OpenShift.

Additionally a number of Micrometer metrics collectors will be automatically registered;

* JvmThreadMetrics
* ProcessorMetrics
* JvmGcMetrics
* JvmMemoryMetrics
* LogbackMetrics
* UptimeMetrics
* FileDescriptorMetrics
* DataSourceMetrics
* RestTemplateMetrics
* TomcatMetrics

To get an overview of how Micrometer works we encourage you to read the Micrometer docs:
https://micrometer.io/docs. Relevant sections are http://micrometer.io/docs/concepts, http://micrometer.io/docs/registry/prometheus and http://micrometer.io/docs/ref/spring/1.5

Outgoing and incomming HTTP metrics have by default percentiles turned on and has buckets between 100ms and 5s.
```properties
management.metrics.distribution.percentiles-histogram.http=true
```

There are default min/max buckets set to 100ms to 5secounds for http and operations metrics

If you would like to change these use the following bean in your code

```java
@Bean
MeterFilter minExpectedHttp() {
    return MeterFilter.minExpected("http", Duration.ofMillis(200));
}
```


## Auto registration of DataSource

If a database is provided on OpenShift there will automatically be a DataSource created from the properties files provided
by the platform. If you want to override to use a specific database you can set the `aurora.db` property if you have more
than one db in your application.

Connection count metrics will also be collected from the DataSource.

## Configuration of the Actuator Endpoints

Most actuator endpoints will be disabled by default;

* auditevents
* heapdump
* metrics
* logfile
* autoconfig
* configprops
* mappings
* beans
* dump
* jolokia

Actuator will also be configured to use the port specified by the `MANAGEMENT_HTTP_PORT` environment variable. The
value of this variable will be set by the Aurora platform when deploying. Security on the actuator endpoints and the
metrics filter will be disabled.