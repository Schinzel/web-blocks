# WebBlocks - A Web and Component Framework for JVM Languages

**Low barrier to entry, high ceiling**  
Build reactive web applications using any JVM language - Kotlin, Java, Scala, Clojure, and more.
It is easy to get started with simple pages while supporting complex web applications as you scale.


## Problems to solve
The problems this project aims to solve are the following.

**Web pages can easily become very large and hard to read and maintain**   
This is solved by a component framework that encourages breaking up a web page into small standalone blocks.

**Finding api endpoints and web pages in a large project can be a hassle**   
This is solved by file system based routing.

**Low threshold to get started**   
There are sample applications that you can copy and base you project on, so that your do not have 
to read a line of instruction to get started.

**Finding resource files that belong to a web page, such as HTML template pages can be unintuitive and in location far away from the web page**   
This is solved by file by reading resources from the source folder of classes when in development
and from the JAR file when running the code from a JAR. 
This way you can have you HTML files in the same folder as your Kotlin files.

**Hot reloading of HTML and JavaScript files has a tendency to be slow and break**   
This is solved by reading HTML and JavaScript files from the source folder of classes when in development which allows for instant updates.

**Writing HTML in Kotlin code is cumbersome.**  
This is solved by a template engine that reads HTML files and replaces placeholders with values.

**In some cases a change in one part of a page by a user should update other parts of the page. 
One want to do this without reloading the whole page. **   
This is solved by: 
1. Blocks have the ability to update themselves.
2. An observer system where a block can notify its observing blocks that they should update themselves.

**Managing state in both client and server leads to synchronization bugs and complexity**   
Modern web frameworks often require maintaining the same data in two places: client-side for instant UI updates and server-side for persistence. 
This dual state management is a common source of bugs including race conditions, stale data, and inconsistent UI states.
This is solved by maintaining a single source of truth on the server. 
When data changes, the server updates the database and relevant blocks automatically update themselves by fetching fresh content from the server, ensuring all components always display consistent, up-to-date information.

**You want a feature based architecture**   
Most frameworks do not allow a feature based architecture. With standalone blocks, 
reading resource files from the source folder of classes, 
and a file system based routing system this framework enables and encourages feature based architecture. 


# Documentation
- [Getting started](doc/user_doc/1_getting_started.md)
- [Routes](doc/user_doc/2_routes.md)
- [Page builder](doc/user_doc/4_page_builder.md)
- [Template engine](doc/user_doc/7_template_engine.md)
- [Error pages](doc/user_doc/8_error_pages.md)
- [Building a JAR](doc/user_doc/10_building_a_jar.md)

# Sample
- [Sample](src/main/kotlin/io/schinzel/sample/README.md)

# Auxiliary Documentation
- [Custom Response Handlers](doc/user_doc/11_custom_response_handlers.md)

# Code Standards
For developers and AIs such as Claude
- [Code Standards](doc/code_standards/code_standards_index.md)
