
# To do
- Logging
    - Type: GET, POST, PUT, DELETE
    - Type: API PAGE
    - Error logging
- Data savers. Can I give page elements the ability to save data? 
- Tester
- subscribe

# How to build and run a JAR
Build a JAR
mvn clean package

Run the JAR
java -jar myJar.jar


# Thoughts on data savers
## IPageElement routing??
Göra IPage aware att består av PageElements och lägga upp router till page elements automatiskt?
Och att PageElements kan ha en data saver?

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