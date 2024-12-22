package io.schinzel.page_elements.endpoint.path

class PagePath(relativePath: String) : IPath {
    override val path: String = if (relativePath == "landing") {
        "/"
    } else {
        relativePath
    }
}