# Task: Rename PageElement to Block (WebBlocks Framework)

## Overview
Refactor the framework to use "Block" terminology instead of "PageElement" throughout the codebase. This aligns with the new framework name "WebBlocks" and creates a cleaner, more intuitive API.

## Renames Required

### 1. POM Configuration
Update `pom.xml`:
```xml
<groupId>io.schinzel</groupId>
<artifactId>webblocks</artifactId>
<name>WebBlocks</name>
<description>A Kotlin web framework for building reactive web applications with blocks</description>
```

### 2. Core Interfaces and Classes

#### In `src/main/kotlin/io/schinzel/page_elements/component/page/`

**File renames:**
- `IPageElement.kt` → `IBlock.kt`
- `ObservablePageElement.kt` → `ObservableBlock.kt`

**Class/Interface renames:**
```kotlin
// IBlock.kt (formerly IPageElement.kt)
interface IBlock {
    suspend fun getHtml(): String
    
    val timeoutMs: Long
        get() = 1_000
}

// ObservableBlock.kt (formerly ObservablePageElement.kt)
abstract class ObservableBlock : IPageApiRoute, IBlock {
    // Update all references in documentation
    /**
     * The purpose of this class is to allow blocks to be observable.
     *
     * This means that on the client side a block can notify its
     * observers (other blocks).
     *
     * Each block has a path and constructor arguments so that these
     * can be used to make a request to the server to get an updated version
     * of the HTML and JavaScript of the block.
     *
     * Each block implements the function getResponse() which returns
     * the HTML and JavaScript of the block.
     */
}
```

### 3. Method Renames

#### PageBuilder.kt
```kotlin
// Change method name and parameter
fun addBlock(block: ObservableBlock): PageBuilder {
    if (rows.isEmpty() || rows.last().columns.isEmpty()) {
        throw Exception("You must add a row and a column before adding a block")
    }
    rows.last().columns.last().blocks.add(block)
    return this
}
```

#### Column.kt
```kotlin
class Column(val span: Int) {
    val blocks: MutableList<ObservableBlock> = mutableListOf()  // renamed from elements

    suspend fun getHtml(): String = supervisorScope {
        val blocksHtml = blocks  // renamed from elementsHtml
            .map { block ->  // renamed from element
                async { 
                    try {
                        withTimeout(block.timeoutMs) {
                            block.getHtml() 
                        }
                    } catch (e: TimeoutCancellationException) {
                        "<div class='error timeout'>Content loading too slow. Please try refreshing.</div>"
                    } catch (e: Exception) {
                        "<div class='error exception'>An unexpected error occurred: ${e.message}</div>"
                    }
                }
            }
            .awaitAll()
            .joinToString("\n")
        
        """
        |<div class="col-$span">
        |  $blocksHtml
        |</div>
        """.trimMargin()
    }
}
```

### 4. HTML Attribute Updates

#### In ObservableBlock.kt
Update the HTML generation to use `data-block` instead of `data-page-element`:
```kotlin
override suspend fun getHtml(): String {
    // Get the HTML of the block
    val blockHtml = this.getResponse()
    // Get the id of the observers
    val observersIdsAsString: String = observers.joinToString(",") { it.guid }
    // Get the path of the block
    val blockEndpointPath = this.getPath()
    // Get the arguments of the block (i.e. parameters with values)
    val constructorArguments = JsonMapper.noIndentMapper
        .writeValueAsString(this.getConstructorArguments())
        .replace("\"", "&quot;")
    return "<div id=\"$guid\" " +
            // Mark the div as a block so that all blocks on the page are easily found
            "data-block " +
            // Add the ids of the observers of this block
            "data-observer-ids=\"$observersIdsAsString\" " +
            // The path to the endpoint of this block
            "data-path=\"$blockEndpointPath\" " +
            // The constructor arguments of this block
            "data-arguments=\"$constructorArguments\"" +
            ">\n" +
            blockHtml +
            "</div>"
}
```

### 5. JavaScript Updates

Update JavaScript files that reference `data-page-element`:

**Rename file:**
- `/src/main/kotlin/io/schinzel/page_elements/component/page/html/page-element-observer.html` → `block-observer.html`

**Update content in `block-observer.html`:**
```javascript
/**
 * Manages observer relationships between blocks.
 * Implements the observer pattern to notify observers of changes.
 * Uses data-observer-ids attribute to track which blocks observe others
 */
class BlockObserver {
    constructor() {
        // Map of block IDs to array of observer IDs
        this.blocks = new Map()
        // For each block
        $("[data-block]").each((_, element) => {
            // Wrap the vanilla DOM element in a jQuery object
            const $element = $(element)
            // Get the id of the block
            const blockId = $element.attr("id")
            // Get the ids of the blocks that observe this block
            const idsOfObservingBlocks = $element.data("observerIds")
                .split(",") // split on comma
                .filter(Boolean) // filter out empty strings
            // Register the block with its observers
            this.blocks.set(blockId, idsOfObservingBlocks)
            console.log(`Registered block ${blockId} with observers: ${idsOfObservingBlocks}`)
        })
    }

    // ... update all other references from element/pageElement to block
}

// Initialize 
window.observer = new BlockObserver()
```

**Update template references:**
Also check and update any references in:
- `/src/main/kotlin/io/schinzel/sample/pages/page_with_page_elements_and_page_api_route/page_elements/update_name_pe/template.html`
  - Change `$(this).closest("[data-page-element]")` to `$(this).closest("[data-block]")`
- `/src/main/kotlin/io/schinzel/page_elements/component/page/html/page-template.html`
  - Change `{{include:page-element-observer.html}}` to `{{include:block-observer.html}}`

### 6. Sample Code Updates

Update all sample implementations to extend `ObservableBlock`:

```kotlin
// Example: GreetingPe.kt → GreetingBlock.kt
class GreetingBlock : ObservableBlock() {
    override suspend fun getResponse(): String {
        return TemplateProcessor(this)
            .addData("firstName", "Pelle")
            .processTemplate("GreetingBlock.html")
    }
}
```

**Directory renames in samples:**
- `/pages/page_with_page_element/` → `/pages/page_with_block/`
- `/pages/page_with_page_elements_and_page_api_route/` → `/pages/page_with_blocks_and_page_api_route/`
- `/page_elements/` subdirectories → `/blocks/` subdirectories

### 7. Documentation Updates

#### Update all documentation files:

**In `/doc/user_doc/4_page_builder.md`:**
```markdown
# Page Builder
The purpose of the page builder is to build HTML pages. 

It is a grid based system based on Bootstrap

- A page consists of a set of rows
- A row consists of a set of columns
- A column consists of a set of blocks

# Block
- A block is a stand alone independent element that returns html
- Each block has its own endpoint so that it can update itself without reloading the whole page
- There is an observer-pattern system where blocks can notify their 
observing blocks that they should update themselves.
- Instant Updates. The HTML and JS resource files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.

Blocks have been designed with a feature based architecture in mind. 
It is encourage that each block has its own directory with all the resources it need such as:
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
    // Add the block GreetingBlock to the page
    .addBlock(GreetingBlock())
    .getHtml()
```
```

#### Update README.md:
- Change all references from "page-elements" to "blocks"
- Update framework description to mention "WebBlocks"

### 8. Package Structure (Optional - Phase 2)

Consider renaming the package structure from:
- `io.schinzel.page_elements` → `io.schinzel.webblocks`

This would be a larger change and could be done in a second phase.

### 9. Class Name Suffix Updates

Consider updating class names from `*Pe` to `*Block`:
- `WelcomePe` → `WelcomeBlock`
- `UpdateNamePe` → `UpdateNameBlock`
- `IntroductionTextPe` → `IntroductionTextBlock`

## Testing

1. Run all tests after refactoring: `mvn test`
2. Test the sample application: `mvn compile exec:exec@run-sample`
3. Verify that:
   - Blocks render correctly
   - Observer pattern still works
   - JavaScript correctly identifies blocks with `data-block` attribute
   - All async functionality continues to work

## Search and Replace Guide

Use these regex patterns for bulk replacements:

1. **PageElement → Block**: `\bPageElement\b` → `Block`
2. **pageElement → block**: `\bpageElement\b` → `block`
3. **page-element → block**: `\bpage-element\b` → `block`
4. **page_element → block**: `\bpage_element\b` → `block`
5. **page element → block**: `\bpage element\b` → `block` (in comments/docs)
6. **Pe class suffix**: `(\w+)Pe\b` → `$1Block` (careful with this one)

Also search for and update:
- Log messages mentioning "page element"
- Comments referencing "page elements"
- Error messages containing "page element"
- Variable names like `pageElementId` → `blockId`

## Success Criteria

- [ ] All references to "PageElement" renamed to "Block"
- [ ] POM updated with new artifact name and description
- [ ] All tests pass
- [ ] Sample application runs correctly
- [ ] Documentation updated consistently
- [ ] JavaScript observer pattern works with new attribute names
- [ ] No references to old terminology remain (search for: PageElement, pageElement, page-element, page_element)

## Notes

- This is a breaking change for users of the framework
- Consider creating a migration guide for existing users
- The async implementation should not be affected by this rename
- Consider renaming the Git repository from `page-elements-kotlin` to `webblocks`
- The local project directory could also be renamed from `/page-elements-kotlin/` to `/webblocks/`
