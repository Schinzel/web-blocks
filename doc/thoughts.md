
# File, paths and packages needed
Source
- Template files
  - Absolute path to the template files
  - Derived from relative to the caller class
- Error template files - Absolute path to the error template files
JAR
  - Path based on package
    - io/schinzel/samples/component/pages/user_account/intro_text
  - Error template files
    - Relative root?
****************************************************************************************************
Can jag skicka in MyWebApp som caller? och sen errors/404.html som filnamn?
****************************************************************************************************

# Error handling
- How to get path to files to read in source dir?
  - Should work in dev mode when using this as a dependency
  - Should work when using this as a JAR
- Error Class Singleton
  - Constructor arguments
    - dir of error pages
    - environment
  - Method arguments
    - ErrorLog, contains error number, message, and stack trace
  

- Error handling with html-
  - A dir "errors"
  - Will need to spend some time on the file reader so this doesn't
    get too complicated
    - Start with only reading the files in the root folder "errors"
  - Subfolder that can override the default error pages
    - These can override the default error pages if in dev mode
  - Environment as a sealed class


# To do
- "/src/main/kotlin/" is hard coded in SourceFileReader
- Should I use path instead of package? 
  - It seems like I convert the package to a path in many places
- Actually, for classpath resources you should always use forward slash '/', even on Windows!
  - Check where I use File.separator if this is correct!
- Check how next does file routing when using the app router
- A common JavaScript class to send data to the server with
  - Error handling
  - Success handling
  - browser console logging
- Make think internal? So is not exposed outside the JAR
- Create a new public repo
- Create a project that uses this framework and see if one can make it work. 
  - Preferably on Heroku
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


# Name
Page elements
WebComponent (no, is already a thing)
WebComp
ElementK
Islands
Swarming - Ants (many small independent things working together)
Swarm
BeeSwarm
Web Swarm
Web Islands 
Web Elements (no, is already a thing)


