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
    private fun processLoops(template: String): String {
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
     * The purpose of this function is to find and process the innermost loop
     * to ensure proper nested loop variable scoping.
     */
    private fun processNextLoop(template: String): String {
        // Find innermost loop by starting from the last loop start marker
        // This ensures nested loops are processed inside-out correctly
        val loopBounds = findInnermostLoop(template)
        if (!loopBounds.isValid()) return template

        val loopInfo = parseLoopDeclaration(template, loopBounds)
        val expandedContent = generateLoopContent(loopInfo)

        return replaceLoopWithContent(template, loopBounds, expandedContent)
    }

    /**
     * The purpose of this function is to locate the boundaries of the innermost loop
     * for correct processing order in nested structures.
     */
    private fun findInnermostLoop(template: String): LoopBounds {
        val lastLoopStart = template.lastIndexOf(LOOP_START_PATTERN)
        if (lastLoopStart == -1) return LoopBounds.invalid()

        val loopEndStart = template.indexOf(LOOP_END_PATTERN, lastLoopStart)
        if (loopEndStart == -1) return LoopBounds.invalid()

        val loopStartEnd = template.indexOf("}}", lastLoopStart)
        return LoopBounds(lastLoopStart, loopStartEnd, loopEndStart)
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

        return processedContent
    }

    /**
     * The purpose of this function is to determine if an object should have
     * its properties processed based on type safety criteria.
     */
    private fun shouldProcessProperties(item: Any): Boolean = item !is String && item !is Number && item !is Boolean

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
    private fun applyStringData(template: String): String =
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
