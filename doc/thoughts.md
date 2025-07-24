
## CSS frameworks

# CSS Framework Comparison for WMS Project

| Rank | Namn | Beskrivning | Look & Feel | AI-friendly |
|------|------|-------------|-------------|-------------|
| 1 | Pico CSS | Ett minimalistiskt, klasslöst CSS-ramverk. Kräver inga klasser alls för att få en snygg grundstil på HTML-element. Lätt och enkelt att använda. | Mycket avskalat och modernt. Neutral och elegant design som passar för enkla, rena gränssnitt. | Very |
| 2 | DaisyUI | Ett komponentbibliotek byggt på Tailwind CSS. Erbjuder färdiga komponenter och teman utan att kräva byggsteg. | Sofistikerat och polerat. Utmärkta färdiga teman inklusive nordisk-inspirerade. Professionell känsla. | Very |
| 3 | Bulma | Ett modernt CSS-ramverk baserat på flexbox. Klassbaserat, men enkelt och intuitivt. Inga JavaScript-komponenter inkluderade. | Luftigt, stilrent och mjukt. Lite mer "vänligt" än Bootstrap – färggladare och rundare formspråk. | Very |
| 4 | Foundation | Ett avancerat och kraftfullt ramverk skapat av Zurb. Stödjer responsiv design, grid-system, och inkluderar många färdiga komponenter. | Funktionellt, affärsinriktat och robust. Känns som Bootstrap men med något mer "enterprise"-touch. | Ok |
| 5 | Shoelace | Ett Web Components-baserat bibliotek (inte bara CSS) med fullt tillgängliga UI-komponenter, designade för att fungera utan ramverkslåsning. | Modern, lite "material design"-liknande känsla. Tydlig estetik, men kan kännas något teknisk/kall. | Ok |
| 6 | Materialize | Ett ramverk som implementerar Googles Material Design. Inkluderar både CSS och JavaScript-komponenter. | Känns som en direkt tolkning av Googles egna appar. Animationsrik, geometrisk och tydligt strukturerad. | So-so |

## Ranking Rationale

- **Pico CSS** tops the list for its semantic HTML approach and zero configuration needs
- **DaisyUI** ranks high for beautiful defaults and excellent theming options
- **Bulma** is very AI-friendly with clear, intuitive class names
- **Foundation** and **Shoelace** are capable but require more configuration
- **Materialize** ranks lowest due to Material Design's opinionated nature conflicting with Scandinavian aesthetic goals





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

