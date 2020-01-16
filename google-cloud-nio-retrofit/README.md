Example of adding the Google Cloud Storage NIO Provider to a legacy jar
=======================================================================

This project shows how to add (retrofit) Google Cloud Storage capabilities to a
jar file for a Java 7 application that uses Java NIO without the need to
recompile.

Note that whenever possible, you instead want to recompile the app and use the normal
dependency mechanism to add a dependency to google-cloud-nio. You can see examples of
this in the
[google-cloud-examples](https://github.com/googleapis/google-cloud-java/tree/master/google-cloud-examples)
project,
[under nio](https://github.com/googleapis/google-cloud-java/tree/master/google-cloud-examples/src/main/java/com/google/cloud/examples/nio).

To run this example:

1. Before running the example, go to the [Google Developers Console][developers-console] to ensure that Google Cloud Storage API is enabled.

2. Log in using gcloud SDK (`gcloud auth login` in command line)

3. Compile the JAR with:
    ```
    mvn package -DskipTests -Dmaven.javadoc.skip=true -Dmaven.source.skip=true
    ```

4.    Run the sample with:

[//]: # ({x-version-update-start:google-cloud-nio:current})
    ```
    java -cp google-cloud-nio/target/google-cloud-nio-0.120.1-alpha-SNAPSHOT-shaded.jar:google-cloud-nio-retrofit/target/google-cloud-nio-retrofit-0.120.1-alpha-SNAPSHOT.jar com.google.cloud.nio.retrofit.ListFilesystems
    ```

    Notice that it lists Google Cloud Storage ("gs"), which it wouldn't if you ran it without the NIO jar:
    ```
    java -cp google-cloud-nio-retrofit/target/google-cloud-nio-retrofit-0.120.1-alpha-SNAPSHOT.jar com.google.cloud.nio.retrofit.ListFilesystems
    ```
[//]: # ({x-version-update-end})

The sample doesn't have anything about Google Cloud Storage in it. It gets that ability from the NIO
jar that we're adding to the classpath. We use the NIO "fat shaded" jar for this purpose as it
also includes the dependencies for google-cloud-nio. 
The underlying mechanism is Java's standard
[ServiceLoader](https://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html)
facility, the
[standard way](http://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/filesystemprovider.html)
to plug in NIO providers like this one.

If you have access to a project's source code you can also simply add google-cloud-nio as
a dependency and let Maven pull in the required dependencies (this is what the NIO unit tests do).
This approach is preferable as the fat jar approach may waste memory on multiple copies of
dependencies.

[developers-console]:https://console.developers.google.com/
