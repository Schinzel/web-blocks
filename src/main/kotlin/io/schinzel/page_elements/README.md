
# To do
- Logging
  - Exec time
  - Call time (UTC, Local)
  - Path
  - Type: GET, POST, PUT, DELETE
  - Type: API PAGE
  - Arguments
  - Response if API
- subscribe

# Documentation

## Sample paths
http://127.0.0.1:5555/?userId=ABC

http://127.0.0.1:5555/api/v1/user-information?userId=123

http://127.0.0.1:5555/sub-dir/sub-dir-2

## Page path
The path to the directory in which a page is located. 
If the page is located in the directory "landing" then the path to the page is "/"

## Routes
Each route is set up with both get and post requests.

## Api path
Prefix: /api
Name of the class in kebab-case

## HTML and JS files
The HTML and JS files are read from the source folder. This means that changes to these files will be reflected in the 
application without the need for a hot reload.

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