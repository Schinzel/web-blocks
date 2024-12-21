package io.schinzel.page_elements.route.path

class ApiPath(relativePath: String) : IPath {
    // pathen = relativePath + klass namne till kebab case
    // Lägg till api först i pathen
    override val path: String = relativePath
}