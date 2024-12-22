
# To do
- Cannot 
  - have a page with path "api"
  - have an api with path "page"
  - cannot have a page nor an api with path "static"
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



# Backup
            // Check if route has arguments
           /* val hasArguments = route.parameters.isNotEmpty()
            // If has arguments
            if (hasArguments) {
                // Create path with parameters
                val pathWithParams = route.parameters.fold(route.getPath()) { path, param ->
                    "$path/{${param.name}}"
                }
                // Register both GET and POST handlers for the same path
                javalin.get(pathWithParams, handler)
                javalin.post(pathWithParams, handler)
            }*/