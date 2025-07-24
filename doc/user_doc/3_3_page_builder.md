# Page Builder
The purpose of the page builder is to build HTML pages.

It is a grid based system based on Bootstrap

- A page consists of a set of rows
- A row consists of a set of columns
- A column consists of a set of blocks

## Block
- A block is a stand alone independent element that returns html
- Each block has its own endpoint so that it can update itself without reloading the whole page
- There is an observer-pattern system where blocks can notify their
observing blocks that they should update themselves.
- Instant Updates. The HTML and JS resource files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.

Blocks have been designed with a feature based architecture in mind.
It is encouraged that each block has its own directory with all the resources it need such as:
- HTML and JavaScript file
- Database readers and writers
- Page API routes
- ...

## Example

```kotlin
PageBuilder()
    .setTitle("Welcome")
    .addRow()
    // The columnSpan determines how wide the column is
    .addColumn(columnSpan = 12)
    // Add the block GreetingBlock to the page
    .addBlock(GreetingBlock())
    .getHtml()
```


## Observer Pattern

Below is how to set up observers

```kotlin
// Create blocks
val welcomeBlock = WelcomeBlock(userId)
val updateNameBlock = UpdateNamePageBlock(userId)
val introTextBlock = IntroductionTextBlock(userId)

// updateNameBlock is the subject that will notify its observers
updateNameBlock
    // welcome-block will observe changes
    // in update-name-block
    .addObserver(welcomeBlock)
    // intro-text-block will observe changes
    // in update-name-block
    .addObserver(introTextBlock)
```
