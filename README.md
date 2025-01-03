# A Web and Component Framework for Kotlin

**Low barrier to entry, high ceiling**  
This project allows you a low threshold to create web application in Kotlin.
It is easy to get started with simple pages while supporting complex, maintainable web applications as you scale.


## Problems to solve
The problems this project aims to solve are the following.

**Web pages can easily become very large and hard to read and maintain**   
This is solved by a component framework that allows for breaking up a web page into small standalone page-elements.

**Finding api endpoints and web pages in a large project can be a hassle**   
This is solved by file system based routing.

**Low threshold to get started**   
There are sample applications that you can copy and base you project on, so that your do not have 
to read a line of instruction to get started.

**Finding resource files that belong to a web page, such as HTML template pages can be unintuitive and in location far away from the web page**   
This is solved by file by reading resources from the source folder of classes when in development
and from the JAR file when running the code from a JAR. This way you can have you HTML files in the same folder as your Kotlin files.

**Hot reloading of HTML and JavaScript files has a tendency to be slow and break**   
This is solved by reading HTML and JavaScript files from the source folder of classes when in development which allows for instant updates.

**Writing HTML in Kotlin code is cumbersome.**.  
This is solved by a template engine that reads HTML files and replaces placeholders with values.

**In some cases a change on a page should update other parts of the page. One want to do this without reloading the whole page**   
This is solved by: 
1. An observer system where a page-element can notify its observers that they should update themselves 
2. Page-elements inherently and automatically have the ability to update themselves.

**You want a feature based architecture**   
Most frameworks do no allow a feature based architecture. With standalone page-elements, 
reading resource files from the source folder of classes, 
and a file system based routing system you can have a feature based architecture. 

The project consists of two parts:
- [Component Framework](src/main/kotlin/io/schinzel/page_elements/component/README.md)
- [Web Framework](src/main/kotlin/io/schinzel/page_elements/web/README.md)

