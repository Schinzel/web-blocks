
- Document that need to change the pom to include html and js files in the jar
- Document as it reads from source folder changes will happen real time. No need for hot reloads for html and js files
- subscribe


# JAR
Build a JAR
mvn clean package

Run the JAR

# Routing 
Skall man göra om med Next med routing
Anger en root katalog.
In this root folder on findes all files named Page. 
The folder of the Page file is the route


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