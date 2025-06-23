# Page with page elements
A grid based system based on Bootstrap
- A page consists of a set of rows
- A row consists of a set of columns
- A column consists of a set of page elements

# Page Element
- Each page-element has its own endpoint for updating individual page-elements on a page
- The page-elements can update themselves independently of the page
- A observer-pattern system where a page-elements can notify is observing page-elements that they should update themselves
- Instant Updates. The HTML and JS files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.
- A template engine

# Example

```kotlin
Page()
    .setTitle("Welcome")
    .addRow() 
    // The columnSpan determines how wide the column is
    .addColumn(columnSpan = 12)
    .addPageElement(GreetingPe())
    .getHtml()
```

