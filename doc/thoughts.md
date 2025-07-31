# Route doc task

# Data savers
- Vem äger vad?
  - components känner till web!
  - är det så
    - att web har ett generellt system för data savers, dvs
      - en data saver registry
      - en route
    - components har kopplar in på detta system genom att implementera
      - html
      - JS
- Sätta upp en route
-

# Att fixa
- Otional parameters
- HTML escaping
- Funkar JAR läsning för multimodule project
- Testa den bygda JAR:en så att paths funkar



# Att fixa 1
class BlockOne : WebBlock() {
override suspend fun getResponse(): IHtmlResponse = html("<p>Hello world from Block One!</p>")
}

Skall det vara WebBlock or PageBlock. Antagligen PageBlock

# Att fixa 2
Verifiera att documentationen är korrekt
/Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md


# Att fixa 3
Förklara när det går från att @PageBlock skall implementera PageBlock och IHtmlRoute


# Att fixa
page-block-api  och de andra prefixen är hårdkodade strängar på många ställen.
Borde vara på ett
Men vilket är det centrala stället som definierar APIerna

RouteDescriptorRegistry är object. Alltid pain i rumpan med test. Kan man sicka in istället?

# File, paths and packages needed
- Expand hard coded error page
- Let request handler return error pages
- Add Tests
- Document error handling / pages
- How about error logging for JSON requests/responses


# To do
- getResponse or something probably need to get it hands on the request object
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

