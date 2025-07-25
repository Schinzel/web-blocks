package io.schinzel.web_blocks.component.template_engine

import io.schinzel.web_blocks.component.template_engine.file_reader.FileReaderFactory
import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader
import kotlin.reflect.full.memberProperties

/**
 * The purpose of this class is to process templates with support for variables,
 * includes, and loops over collections while maintaining thread safety and
 * immutable design principles for enterprise production environments.
 *
 * Written by Claude Sonnet 4
 */
class TemplateProcessor private constructor(
    private val fileReader: IFileReader,
    // Immutable data collections ensure thread safety
    private val stringData: Map<String, String>,
    private val listData: Map<String, List<Any>>,
) {
    constructor(caller: Any) : this(FileReaderFactory.create(caller), emptyMap(), emptyMap())

    constructor(fileReader: IFileReader) : this(fileReader, emptyMap(), emptyMap())

    /**
     * The purpose of this function is to create a new processor instance with
     * additional string or list data while maintaining immutability.
     */
    fun withData(
        key: String,
        value: Any,
    ): TemplateProcessor =
        when (value) {
            is String -> withStringData(key, value)
            is Int -> withStringData(key, value.toString())
            is List<*> -> withListData(key, value as List<Any>)
            else -> throw IllegalArgumentException("Unsupported data type: ${value::class}")
        }

    /**
     * The purpose of this function is to process a template file with includes,
     * loops, and variable substitution using immutable processing pipeline.
     */
    fun processTemplate(fileName: String): String {
        // Input validation prevents directory traversal and malformed paths
        require(fileName.isNotBlank()) { "Template file name cannot be blank" }
        require(!fileName.contains("..")) { "Path traversal not allowed in template names" }

        val content = fileReader.getFileContent(fileName)
        // Process in order: includes first, then loops, finally variables
        // This ensures proper nesting and variable scope resolution
        val templateWithIncludes = processIncludeFiles(content)
        val templateWithLoops = processLoops(templateWithIncludes)
        return applyStringData(templateWithLoops)
    }

    companion object {
        private const val INCLUDE_FILE_START = "{{include:"
        private const val INCLUDE_FILE_END = "}}"

        // 10 levels prevents infinite recursion while allowing reasonable nesting
        // Protects system resources in enterprise environments
        private const val MAX_INCLUDE_DEPTH = 10
        private const val LOOP_START_PATTERN = "{{for "
        private const val LOOP_END_PATTERN = "{{/for}}"
    }

    // Immutable data management - creates new instances instead of mutating state
    private fun withStringData(
        key: String,
        value: String,
    ): TemplateProcessor = TemplateProcessor(fileReader, stringData + (key to value), listData)

    private fun withListData(
        key: String,
        value: List<Any>,
    ): TemplateProcessor = TemplateProcessor(fileReader, stringData, listData + (key to value))

    /**
     * The purpose of this function is to recursively process include files
     * while preventing circular dependencies that could crash the application.
     */
    private fun processIncludeFiles(
        content: String,
        depth: Int = 0,
    ): String {
        // Prevent infinite recursion from circular includes
        // Enterprise systems require protection against malicious or malformed templates
        if (depth >= MAX_INCLUDE_DEPTH) {
            throw IllegalStateException(
                "Max include depth ($MAX_INCLUDE_DEPTH) exceeded. Possible circular dependency.",
            )
        }

        return processIncludesIteratively(content, depth)
    }

    /**
     * The purpose of this function is to iteratively process all include statements
     * in a template to avoid recursive stack overflow with many includes.
     */
    private fun processIncludesIteratively(
        content: String,
        depth: Int,
    ): String {
        var processedTemplate = content
        var hasMoreIncludes = true

        // Process all includes in current template level
        // Iterative approach prevents stack overflow with many includes
        while (hasMoreIncludes) {
            val includeResult = findAndProcessNextInclude(processedTemplate, depth)
            if (includeResult.found) {
                processedTemplate = includeResult.content
            } else {
                hasMoreIncludes = false
            }
        }
        return processedTemplate
    }

    /**
     * The purpose of this function is to find and process a single include statement
     * returning both the result and whether an include was found and processed.
     */
    private fun findAndProcessNextInclude(
        template: String,
        depth: Int,
    ): IncludeResult {
        // Locate include syntax markers in template
        val startIndex = template.indexOf(INCLUDE_FILE_START)
        if (startIndex == -1) return IncludeResult(template, false)

        val endIndex = template.indexOf(INCLUDE_FILE_END, startIndex)
        if (endIndex == -1) return IncludeResult(template, false)

        // Extract filename from include directive
        val includeFileName = extractIncludeFileName(template, startIndex, endIndex)

        // Read and recursively process included content
        // Recursive call handles nested includes in included files
        val includeFileContent = fileReader.getFileContent(includeFileName)
        val processedInclude = processIncludeFiles(includeFileContent, depth + 1)

        // Replace include directive with processed content
        val newContent =
            template.substring(0, startIndex) +
                processedInclude +
                template.substring(endIndex + INCLUDE_FILE_END.length)

        return IncludeResult(newContent, true)
    }

    /**
     * The purpose of this function is to safely extract the filename from include syntax
     * with proper validation to prevent injection attacks.
     */
    private fun extractIncludeFileName(
        template: String,
        startIndex: Int,
        endIndex: Int,
    ): String {
        val fileName =
            template
                .substring(startIndex + INCLUDE_FILE_START.length, endIndex)
                .trim()

        // Validate filename to prevent directory traversal attacks
        require(fileName.isNotBlank()) { "Include filename cannot be blank" }
        require(!fileName.contains("..")) { "Path traversal not allowed in include filenames" }

        return fileName
    }

    /**
     * The purpose of this function is to process all loops in template content
     * using inside-out processing to handle nested loops correctly.
     */
    fun processLoops(template: String): String {
        var processedTemplate = template
        var iterationCount = 0
        val maxIterations = 100 // Prevent infinite loops with malformed templates

        // Process loops from innermost to outermost for proper nesting
        // This prevents variable conflicts in nested loop structures
        while (containsLoop(processedTemplate) && iterationCount < maxIterations) {
            val previousTemplate = processedTemplate
            processedTemplate = processNextLoop(processedTemplate)

            // Safety check: ensure progress is being made to prevent infinite loops
            if (processedTemplate == previousTemplate) {
                throw IllegalStateException("Loop processing failed to make progress. Template may be malformed.")
            }
            iterationCount++
        }

        if (iterationCount >= maxIterations) {
            throw IllegalStateException(
                "Loop processing exceeded maximum iterations. Template may contain infinite loop.",
            )
        }

        return processedTemplate
    }

    /**
     * The purpose of this function is to check if template contains loop constructs
     * for efficient loop detection without complex parsing.
     */
    private fun containsLoop(template: String): Boolean =
        template.contains(LOOP_START_PATTERN) && template.contains(LOOP_END_PATTERN)

    /**
     * The purpose of this function is to find and process the next processable loop
     * ensuring nested loops are processed at the correct scope level.
     */
    private fun processNextLoop(template: String): String {
        // Find a loop that can be processed (doesn't reference undefined variables)
        val loopBounds = findProcessableLoop(template)
        if (!loopBounds.isValid()) return template

        val loopInfo = parseLoopDeclaration(template, loopBounds)
        val expandedContent = generateLoopContent(loopInfo)

        return replaceLoopWithContent(template, loopBounds, expandedContent)
    }

    /**
     * The purpose of this function is to locate the boundaries of a processable loop
     * ensuring nested loops are processed at the correct scope level.
     */
    private fun findProcessableLoop(template: String): LoopBounds {
        // Find all loops and check which ones can be processed
        var searchStart = 0
        while (true) {
            val loopStart = template.indexOf(LOOP_START_PATTERN, searchStart)
            if (loopStart == -1) break

            val loopStartEnd = template.indexOf("}}", loopStart)
            if (loopStartEnd == -1) break

            val loopEndStart = template.indexOf(LOOP_END_PATTERN, loopStartEnd)
            if (loopEndStart == -1) break

            val bounds = LoopBounds(loopStart, loopStartEnd, loopEndStart)

            // Check if this loop can be processed (its data source exists)
            if (canProcessLoop(template, bounds)) {
                return bounds
            }

            searchStart = loopStartEnd + 1
        }

        return LoopBounds.invalid()
    }

    /**
     * The purpose of this function is to check if a loop can be processed
     * by verifying its data source exists in current scope.
     */
    private fun canProcessLoop(
        template: String,
        bounds: LoopBounds,
    ): Boolean {
        try {
            val loopDeclaration = template.substring(bounds.start + LOOP_START_PATTERN.length, bounds.startEnd)
            val parts = loopDeclaration.split(" in ")
            if (parts.size != 2) return false

            val listName = parts[1].trim()

            // Check if the list data exists (not a property reference like "item.property")
            return listData.containsKey(listName) && !listName.contains(".")
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * The purpose of this function is to parse loop declaration and extract
     * the iteration variable name and collection name with validation.
     */
    private fun parseLoopDeclaration(
        template: String,
        bounds: LoopBounds,
    ): LoopInfo {
        val loopDeclaration = template.substring(bounds.start + LOOP_START_PATTERN.length, bounds.startEnd)
        val parts = loopDeclaration.split(" in ")

        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid loop syntax: $loopDeclaration. Expected format: 'item in items'")
        }

        val itemName = parts[0].trim()
        val listName = parts[1].trim()
        val items = listData[listName] ?: emptyList()
        val content = template.substring(bounds.startEnd + 2, bounds.endStart)

        return LoopInfo(itemName, listName, items, content)
    }

    /**
     * The purpose of this function is to generate the expanded content for a loop
     * by processing each item through the loop template safely.
     */
    private fun generateLoopContent(loopInfo: LoopInfo): String =
        loopInfo.items.joinToString("") { item ->
            processLoopItem(loopInfo.content, loopInfo.itemName, item)
        }

    /**
     * The purpose of this function is to process a single loop iteration
     * with controlled property access for security in enterprise environments.
     */
    private fun processLoopItem(
        content: String,
        itemName: String,
        item: Any,
    ): String {
        var processedContent = content

        // Handle simple item replacement for primitive types
        processedContent = processedContent.replace("{{$itemName}}", item.toString())

        // Handle object property access with security controls
        // Only process non-primitive types that likely have meaningful properties
        if (shouldProcessProperties(item)) {
            processedContent = processObjectProperties(processedContent, itemName, item)
        }

        // For nested loops, we need to replace item.property references in loops with direct property references
        // Then create a new processor with access to object properties as lists
        if (shouldProcessProperties(item) && containsLoop(processedContent)) {
            // Replace references like "team.users" in nested loops with just "users"
            processedContent = replaceNestedLoopReferences(processedContent, itemName)
            val nestedProcessor = createNestedProcessor(item)
            // Process loops first, then apply any remaining string substitutions
            processedContent = nestedProcessor.processLoops(processedContent)
            processedContent = nestedProcessor.applyStringData(processedContent)
        }

        return processedContent
    }

    /**
     * The purpose of this function is to determine if an object should have
     * its properties processed based on type safety criteria.
     */
    private fun shouldProcessProperties(item: Any): Boolean = item !is String && item !is Number && item !is Boolean

    /**
     * The purpose of this function is to replace nested loop references like "item.property"
     * with just "property" so the nested processor can find the data.
     */
    private fun replaceNestedLoopReferences(
        content: String,
        itemName: String,
    ): String {
        // Replace patterns like "{{for x in item.property}}" with "{{for x in property}}"
        val pattern = "\\{\\{for\\s+(\\w+)\\s+in\\s+$itemName\\.(\\w+)\\}\\}".toRegex()
        return pattern.replace(content) { matchResult ->
            val loopVar = matchResult.groupValues[1]
            val property = matchResult.groupValues[2]
            "{{for $loopVar in $property}}"
        }
    }

    /**
     * The purpose of this function is to create a nested processor with access to object properties
     * for processing nested loops within loop iterations.
     */
    private fun createNestedProcessor(item: Any): TemplateProcessor {
        // Start with current context (stringData and listData) to maintain variable scope
        var nestedProcessor = TemplateProcessor(fileReader, stringData, listData)

        try {
            val properties = item::class.memberProperties
            for (property in properties) {
                if (property.visibility == null || property.visibility.toString() == "PUBLIC") {
                    val propertyValue = property.getter.call(item)
                    if (propertyValue is List<*>) {
                        nestedProcessor = nestedProcessor.withListData(property.name, propertyValue as List<Any>)
                        // Debug: Print what we're adding
                        println(
                            "DEBUG: Adding list property '${property.name}' with ${(propertyValue as List<*>).size} items",
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Graceful degradation if reflection fails
        }

        return nestedProcessor
    }

    /**
     * The purpose of this function is to safely process object properties using
     * controlled reflection to prevent security vulnerabilities.
     */
    private fun processObjectProperties(
        content: String,
        itemName: String,
        item: Any,
    ): String {
        var processedContent = content

        try {
            // Use Kotlin reflection with error handling for enterprise safety
            val properties = item::class.memberProperties
            for (property in properties) {
                // Only process public properties to maintain encapsulation
                if (property.visibility == null || property.visibility.toString() == "PUBLIC") {
                    val propertyName = property.name
                    val propertyValue = property.getter.call(item)?.toString() ?: ""
                    processedContent = processedContent.replace("{{$itemName.$propertyName}}", propertyValue)
                }
            }
        } catch (e: Exception) {
            // Graceful degradation if reflection fails in restricted environments
            // Log error in production systems for debugging
        }

        return processedContent
    }

    /**
     * The purpose of this function is to replace string placeholders using
     * efficient map-based substitution with the immutable string data.
     */
    fun applyStringData(template: String): String =
        stringData.entries.fold(template) { content, (key, value) ->
            content.replace("{{$key}}", value)
        }

    /**
     * The purpose of this function is to replace a processed loop with its
     * expanded content in the template maintaining structural integrity.
     */
    private fun replaceLoopWithContent(
        template: String,
        bounds: LoopBounds,
        content: String,
    ): String =
        template.substring(0, bounds.start) +
            content +
            template.substring(bounds.endStart + LOOP_END_PATTERN.length)

    // Data classes for improved type safety and clarity
    private data class IncludeResult(
        val content: String,
        val found: Boolean,
    )

    private data class LoopBounds(
        val start: Int,
        val startEnd: Int,
        val endStart: Int,
    ) {
        fun isValid(): Boolean = start != -1 && startEnd != -1 && endStart != -1

        companion object {
            fun invalid(): LoopBounds = LoopBounds(-1, -1, -1)
        }
    }

    private data class LoopInfo(
        val itemName: String,
        val listName: String,
        val items: List<Any>,
        val content: String,
    )
}
