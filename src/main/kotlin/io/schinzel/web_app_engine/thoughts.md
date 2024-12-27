
# To do
- subscribe
  - The next piece of the puzzle is to find path
    - Can IResponseHandle return path with the help of  IResponseHandlerDescriptor
      - I think so if we have access to endpointPackage
        - Make endpointPackage into to a public property 
- Break up ResponseHandlerMapping?
  - One class that handles setting up the routes (if any)
  - One class the handles requests and responses (if any)
- Logging lacks:
  - Static resources
  - when a Page or Endpoint is not found
- Forbidden paths
  - have a page with path "api"
  - have an api with path "page"
  - cannot have a page nor an api with path "static"
- Tester & doc
- Create an html page that returns all the endpoints


# How to build and run a JAR
- Build a JAR: mvn clean package 
- Run the JAR: java -jar myJar.jar


# Super central for the project
- The IPage and IPageElement class is the central class in the project.
- The file based routing


# Name
ElementK





# Subscribe
- WebAppEngine does not know about BootstrapPage
- BootstrapPage 
  - knows about WebAppEngine 
  - Owns IPageElement
  - IPageElement either 
    - implements IWebPageEndpoint so that each IPageElement gets a route
    - or creates its own implementation of IRequestProcessor
    - val subscribers = mutableList<IPageElement>


IPageElement has an Endpoint.
updateSubscribers()

IPage has a list of IPageElements.
Each IPageElement will be surrounded by a giv with a unique id (possible a guid)
Last on the page JS will be generated with possible a map or a list 
that maps the unique id to the endpoint of the IPageElement.

The is also the actual subscribe function. 
Could be on the page or on the IPageElement.

Step 1) Surround IPageElement with a div with a unique id

Skall man anta ett grid system?

Perhaps one should see it as two systems
1) The routing and the file system that provides an url to each page element
2) The bootstrap page system 
   1) Each page element has a div with unique id
   2) There is a footer that sets up the mapping between the unique id and the endpoint

### Where does the subscribe function go?
Perhaps that belongs to the Bootstrap implementation of IPage. 
IBootstrapPageElement implements IPageElement and adds the subscribe function or 
a val subscribers = mutableListOf<IBootstrapPageElement>()


```kotlin

```
