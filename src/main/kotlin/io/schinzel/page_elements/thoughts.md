
# To do
- Data savers
  - Can I give page elements the ability to save data? 
- subscribe
- Logging lacks:
  - Static resources
  - when a Page or Endpoint is not found
- Forbidden paths
  - have a page with path "api"
  - have an api with path "page"
  - cannot have a page nor an api with path "static"
- Tester & doc

# How to build and run a JAR
- Build a JAR: mvn clean package 
- Run the JAR: java -jar myJar.jar


# Thoughts on data savers
Should use the same thoughts on routing. I.e. routes to data savers
is defined by and mirrors the file structure.
user_account/my_pe
- Man vill göra två saker
  - Spara data
  - Hämta data, förnya ett page element då ett annat sparat data, i.e. subscribe

IPageElement
IDataSaver

((kall man ha API:er inna i page elements dirs?
  Om vi kör fullt ut FBA så är det fallet. Men behövs det??
     Inte lösa det om det inte är ett problem)) 

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

