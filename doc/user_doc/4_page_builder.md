# Page Builder
The purpose of the page builder is to build HTML pages. 

It is a grid based system based on Bootstrap

- A page consists of a set of rows
- A row consists of a set of columns
- A column consists of a set of page elements

# Page Element
- A page element is a stand alone independent element that returns a html
- Each page-element has its own endpoint so that it can update itself without reloading the whole page
- There is an observer-pattern system where a page-elements can notify its 
observing page-elements that they should update themselves.
- Instant Updates. The HTML and JS resource files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.

Page elements have been designed with a feature based architecture in mind. 
It is encourage that each page element has its own directory with all the resources it need such as:
- HTML and JavaScript file
- Database readers and writers
- Page API routes
- ...

# Example

```kotlin
PageBuilder()
    .setTitle("Welcome")
    .addRow() 
    // The columnSpan determines how wide the column is
    .addColumn(columnSpan = 12)
    // Add the page element GreetingsPe to the page
    .addPageElement(GreetingPe())
    .getHtml()
```

