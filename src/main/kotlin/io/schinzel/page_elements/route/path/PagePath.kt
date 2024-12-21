package io.schinzel.page_elements.route.path

class PagePath(relativePath: String) : IPath {
    override val path: String

    init {
        path = if (relativePath == "landing") {
            "/"
        } else {
            relativePath
        }
    }
}