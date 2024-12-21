
To do
- Set up API
  - Path prefix "/api" 
    - Should I add that you can have Api folder?
  - include the class name ex Api suffix if there is one
- Change from PascalCase to kebab-case for pages and api calls
- Logging
  - Exec time
  - Call time (UTC, Local)
  - Path
  - Type: GET, POST, PUT, DELETE
  - Type: API PAGE
  - Arguments
  - Response if API
- Test gradle?
- Document that need to change the pom to include html and js files in the jar
- Document as it reads from source folder changes will happen real time. No need for hot reloads for html and js files
- subscribe


# JAR
Build a JAR
mvn clean package

Run the JAR
java -jar myJar.jar

# Gradle
## Installation
brew install gradle
gradle wrapper

# Routing 
Go with the IPage solution. 
Find all IPages. 
For each IPage, create a route.


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