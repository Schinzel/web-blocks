# Custom HTML generation

It is easy to build your own or wrap existing HTML generators.

All they have to do is to
- The class has to implement the interface `IHtmlRoute`
- which requires the function `fun getResponse(): IHtmlResponse`
