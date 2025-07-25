# Add Loop Support to Template Engine (AST Version)

## Goal
Refactor template engine to use AST-based approach and add loop functionality to iterate over lists and collections.

## Current State
Previous implementation attempted string-replacement approach with nested loop support.
Basic loops work, but nested loops fail due to architectural limitations.
Comprehensive test suite exists with 5 failing tests.
Current implementation uses immutable design patterns - **maintain immutability in AST version**.

## Motivation
Current monolithic string-replacement approach has limitations:
- Feature interactions (if-then + loops) create unsolvable ordering problems
- Performance issues with multiple string scans
- Difficult to extend with new features

## Required reading
- Must read [code standard](../code_standards/_index.md)

## Architecture: AST-Based Template Engine

### Core Components

#### 1. Node Types (AST Structure)
```kotlin
sealed class TemplateNode
data class TextNode(val text: String) : TemplateNode()
data class VariableNode(val name: String) : TemplateNode()
data class ForLoopNode(val variable: String, val list: String, val body: List<TemplateNode>) : TemplateNode()
data class IncludeNode(val fileName: String) : TemplateNode()
```

#### 2. Tokenizer
Converts template string into tokens with position tracking for error reporting:
```kotlin
sealed class Token
data class TextToken(val text: String, val line: Int, val column: Int) : Token()
data class TagToken(val content: String, val line: Int, val column: Int) : Token()  // Everything inside {{ }}
```

#### 3. Parser System
```kotlin
interface NodeParser {
    fun canParse(tagContent: String): Boolean
    fun parse(tagContent: String, tokenStream: TokenStream, mainParser: TemplateParser): TemplateNode
}

class ForLoopParser : NodeParser {
    // Parses "for user in users" syntax
    // Calls mainParser.parseUntil("/for") to get body nodes
}

class VariableParser : NodeParser {
    // Parses simple variables and property access
}

class IncludeParser : NodeParser {
    // Parses "include: filename.html"
}

// Main parser needs method for block parsers
class TemplateParser {
    fun parseUntil(stopTag: String): List<TemplateNode> {
        // Parse nodes until encountering stopTag
    }
}
```

#### 4. Evaluator System
```kotlin
interface NodeEvaluator<T : TemplateNode> {
    fun evaluate(node: T, context: ProcessingContext, evaluator: TemplateEvaluator): String
}

class ForLoopEvaluator : NodeEvaluator<ForLoopNode> {
    // Handles loop iteration and context scoping
}

class VariableEvaluator : NodeEvaluator<VariableNode> {
    // Handles variable lookup and property access via reflection
}

class IncludeEvaluator : NodeEvaluator<IncludeNode> {
    // Handles file inclusion with circular dependency protection
}
```

#### 5. Main Template Processor
```kotlin
class TemplateProcessor(
    private val fileReader: IFileReader,
    private val parsers: List<NodeParser>,
    private val evaluators: Map<KClass<*>, NodeEvaluator<*>>
) {
    fun processTemplate(fileName: String): String {
        val content = fileReader.getFileContent(fileName)
        val tokens = tokenize(content)
        val ast = parse(tokens)
        return evaluate(ast, ProcessingContext(stringData, listData))
    }
    
    private fun evaluate(node: TemplateNode, context: ProcessingContext): String {
        val evaluator = evaluators[node::class] 
            ?: throw IllegalStateException("No evaluator for ${node::class.simpleName}")
        
        @Suppress("UNCHECKED_CAST")
        return (evaluator as NodeEvaluator<TemplateNode>).evaluate(node, context, this)
    }
}
```

#### 6. Context Management
Immutable, layered context for proper variable scoping:
```kotlin
class ProcessingContext(
    private val data: Map<String, Any>, 
    private val parent: ProcessingContext? = null
) {
    fun lookup(name: String): Any? {
        return data[name] ?: parent?.lookup(name)
    }
    
    fun with(key: String, value: Any): ProcessingContext {
        // Creates new context for deeper scope
        return ProcessingContext(mapOf(key to value), parent = this)
    }
}
```

## Syntax (Same as before)
```html
{{for item in items}}
    <!-- content -->
{{/for}}
```

## Requirements (Same as before)

### 1. Simple Lists
Support iteration over simple lists (strings, numbers)

### 2. Object Lists
Support iteration over DTOs/objects with property access

### 3. Nested Loops
Support nested loops with proper scoping

## Implementation Benefits
- **Extensible**: Easy to add if-then, unless, filters, etc.
- **Performance**: Single parse pass, efficient evaluation with AST caching
- **Maintainable**: Each component has single responsibility
- **Testable**: Can test parsers and evaluators independently
- **No ordering issues**: Tree structure handles all nesting naturally
- **Superior error reporting**: "Undefined variable 'user.emai' at template.html (line 15, column 10)"

## Implementation Steps
1. Create node type hierarchy (immutable data classes)
2. Implement tokenizer
3. Create parser framework and individual parsers
4. Create evaluator framework and individual evaluators
5. Wire together in new TemplateProcessor (immutable design)
6. Migrate existing functionality (includes, variables) to new system
7. Add loop support
8. **Keep existing test suite** - comprehensive tests already exist including edge cases

## Files to Create
- `/src/.../template_engine/ast/TemplateNode.kt` - Node types (with position info)
- `/src/.../template_engine/ast/Token.kt` - Token types with line/column
- `/src/.../template_engine/ast/Tokenizer.kt` - Tokenization logic with position tracking
- `/src/.../template_engine/parser/NodeParser.kt` - Parser interface
- `/src/.../template_engine/parser/TemplateParser.kt` - Main parser with parseUntil method
- `/src/.../template_engine/parser/ForLoopParser.kt` - Loop parser
- `/src/.../template_engine/parser/VariableParser.kt` - Variable parser
- `/src/.../template_engine/parser/IncludeParser.kt` - Include parser
- `/src/.../template_engine/evaluator/NodeEvaluator.kt` - Evaluator interface
- `/src/.../template_engine/evaluator/ForLoopEvaluator.kt` - Loop evaluator with scoping
- `/src/.../template_engine/evaluator/VariableEvaluator.kt` - Variable evaluator
- `/src/.../template_engine/evaluator/IncludeEvaluator.kt` - Include evaluator with caching
- `/src/.../template_engine/context/ProcessingContext.kt` - Immutable layered context
- `/src/.../template_engine/TemplateProcessor.kt` - Main processor (refactored)

## Documentation
- Updated: `/Users/schinzel/code/web-blocks/doc/user_doc/3_2_template_engine.md`

## Testing
- Test tokenizer with various inputs
- Test each parser independently
- Test each evaluator independently
- Test complete integration
- Test edge cases (empty lists, null values, malformed syntax)
- Test mixed templates (loops + variables + includes)
- Test error handling and meaningful error messages

## Future Extensions Made Easy
With this architecture, adding new features is straightforward:
- **If-then**: Add IfNode, IfParser, IfEvaluator
- **Filters**: Add to VariableNode, handle in VariableEvaluator
- **Custom tags**: Just add new parser/evaluator pair
- **Debugging**: Can add source position to nodes for better errors

## Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test
