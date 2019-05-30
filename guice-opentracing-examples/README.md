# Guice OpenTracing CDI - Example

## The example

This example shows the different ways to use the OpenTracing CDI integration. It features
getting a `Tracer` injected into your components

## Running

In order to visualize the spans, you'll need an instance of Jaeger running locally.
Any other OpenTracing tracer is supported: all it requires is to change the `pom.xml`
to remove Jaeger's dependencies and add your `TracerResolver` compatible Tracer.

Jaeger can be run via Docker as follows:
```
docker-compose up -d
```

Once that is done, run the Example main method
