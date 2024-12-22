
# To do
- Logging
    - Exec time
    - Call time (UTC, Local)
    - Path
    - Type: GET, POST, PUT, DELETE
    - Type: API PAGE
    - Arguments
    - Response if API. No response if page
- subscribe

# How to build and run a JAR
Build a JAR
mvn clean package

Run the JAR
java -jar myJar.jar


# Thoughts on data savers
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