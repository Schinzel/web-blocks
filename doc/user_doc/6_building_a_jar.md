# Building a JAR
When building a JAR the HTML and JS files need to be included in the JAR.
This is done by adding the following to the pom.xml file.
```xml
<build>
  <resources>
    <resource>
      <directory>src/main/kotlin</directory>
      <includes>
        <!-- Include html and js files in the jar -->
        <include>**/*.html</include>
        <include>**/*.js</include>
      </includes>
    </resource>
    <!-- Keep the default resources directory if you have one -->
    <resource>
      <directory>src/main/resources</directory>
    </resource>
  </resources>
  [...]
</build>
```
