# Sample

Below directories are relative to this README file.
Start the main file [MyWebApp.kt](MyWebApp.kt).

## Page Routes

A simple HTML page.
- [pages/simple_page/ThePage.kt](../../src/main/kotlin/io/schinzel/sample/pages/simple_page/ThePage.kt )
- http://127.0.0.1:5555/simple-page

Landing page. Returns the page in the directory `landing`.
- [pages/landing/LandingPage.kt](../../src/main/kotlin/io/schinzel/sample/pages/landing/LandingPage.kt)
- http://127.0.0.1:5555/

A page with one simple block.
- [pages/page_with_block/ThePage.kt](../../src/main/kotlin/io/schinzel/sample/pages/page_with_block/ThePage.kt)
- http://127.0.0.1:5555/page-with-block?user-id=123222

A page with three blocks and a page route.
Two of the blocks observe one block.
This means if the observed block changes, the observing blocks update themselves.
- [pages/page_with_blocks_and_page_api_route/WelcomePage.kt](../../src/main/kotlin/io/schinzel/sample/pages/page_with_blocks_and_page_api_route/WelcomePage.kt)
- http://127.0.0.1:5555/page-with-blocks-and-page-api-route?user-id=123222

A page with custom status code (201) using HtmlResponseBuilder.
- [pages/page_with_custom_status/ThePageWithStatus.kt](../../src/main/kotlin/io/schinzel/sample/pages/page_with_custom_status/ThePageWithStatus.kt)
- http://127.0.0.1:5555/page-with-custom-status

A page with custom headers using HtmlResponseBuilder.
- [pages/page_with_headers/ThePageWithHeaders.kt](../../src/main/kotlin/io/schinzel/sample/pages/page_with_headers/ThePageWithHeaders.kt)
- http://127.0.0.1:5555/page-with-headers

A page demonstrating Java-friendly builder usage.
- [pages/java_style_page/JavaStylePage.kt](../../src/main/kotlin/io/schinzel/sample/pages/java_style_page/JavaStylePage.kt)
- http://127.0.0.1:5555/java-style-page


## API Routes

A simple API route.
- [api/UserPets.kt](../../src/main/kotlin/io/schinzel/sample/api/UserPets.kt)
- http://127.0.0.1:5555/api/user-pets

An API route with one parameter.
- [api/UserInformationEndpoint.kt](../../src/main/kotlin/io/schinzel/sample/api/UserInformationEndpoint.kt)
- http://127.0.0.1:5555/api/user-information-endpoint?user-id=123

An API route that throws an error.
- [api/ApiRouteThatThrowsError.kt](../../src/main/kotlin/io/schinzel/sample/api/ApiThatThrowsError.kt)
- http://127.0.0.1:5555/api/api-route-that-throws-error

An API route with custom headers using JsonResponseBuilder. It sets the headers X-Total-Count, X-Rate-Limit-Remaining and X-User-Type
- [api/UserApiWithHeaders.kt](../../src/main/kotlin/io/schinzel/sample/api/UserApiWithHeaders.kt)
- http://127.0.0.1:5555/api/user-api-with-headers?user-id=123
