
To do
- Logging
  - Exec time
  - Call time (UTC, Local)
  - Path
  - Type: GET, POST, PUT, DELETE
  - Type: API PAGE
  - Arguments
  - Response if API
- Document that need to change the pom to include html and js files in the jar
- Document as it reads from source folder changes will happen real time. No need for hot reloads for html and js files
- subscribe

# Documentation
## Page path
The path to the directory in which a page is located. 
If the page is located in the directory "landing" then the path to the page is "/"

## Api path
Prefix: /api
Name of the class in kebab-case

## Building a JAR
When building a JAR the html and js files need to be included in the JAR.
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
# JAR
Build a JAR
mvn clean package

Run the JAR
java -jar myJar.jar


# Data savers
## Path 1
PageElements kan ha en data sparare

Det finns en map<PageName,Page>
Sidor har en map<PageElementName,PageElement>

Det finns ett api save-data/{pageName}/{pageElementName}
innehåller kanske JSON för data att spara

Hitta sidan i mapen
Hitta elementet i sidan
Spara data i elementet


## Path 2

Man registrerar sin datasparare i en singleton som håller datasparare

En map för datasparare
map<GUID, IDataSaver>

interface IDataSaver {
    fun save(data: String)
}

api/save-data/{guid}
Postar json med data, behöver inte vara json antar jag.