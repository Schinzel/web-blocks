
# To do
- Data savers
  - Can I give page elements the ability to save data? 
- subscribe
- Break up file reading and template engine
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

# Name
ElementK


# Subscribe
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