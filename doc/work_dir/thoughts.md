

# File, paths and packages needed
- Expand hard coded error page
- Let request handler return error pages
- Add Tests
- Document error handling / pages
- How about error logging for JSON requests/responses


# To do
- Need to document that both GET and POST are set up automatically for (all three routes?)
- "/src/main/kotlin/" is hard coded in SourceFileReader
- Is this correct ObservablePageElement : IPageApiRoute, IPageElement?
- Should resource files be snake-case or camel-case?
- Session variable? 
- Actually, for classpath resources you should always use forward slash '/', even on Windows!
  - Check where I use File.separator if this is correct!
- Check how next does file routing when using the app router
- A common JavaScript class to send data to the server with
  - Error handling
  - Success handling
  - browser console logging
- Annotations instead of implementation of interface?
- Make things internal? So is not exposed outside the JAR
- Create a human-readable html page-that contains documentation of all the endpoints

# Maybe to do
- Break up ResponseHandlerMapping?
  - One class that handles setting up the routes (if any)
  - One class the handles requests and responses (if any)
- Path parameters?
- Logging lacks:
  - Static resources
  - when a Page or Endpoint is not found


# How to build and run a JAR
- Build a JAR: mvn clean package 
- Run the JAR: java -jar myJar.jar


