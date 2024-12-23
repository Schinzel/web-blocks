
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

# Name
ElementK


# Subscribe
IPageElement has an Endpoint.
updateSubscribers()