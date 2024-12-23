package io.schinzel.page_elements.route_mapping.path

class PageRoute(pagePath: String) : IRoute {
    override val path: String = if (pagePath == "landing") {
        "/"
    } else {
        pagePath
    }
}