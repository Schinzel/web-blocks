# Sample

Below directories are relative to this README file.
Start the main file [MyWebApp.kt](MyWebApp.kt).

## Page Routes

A simple HTML page. 
- [pages/simple_page/ThePage.kt](pages/simple_page/ThePage.kt)
- http://127.0.0.1:5555/simple-page

Landing page. Returns the page in the directory `landing`.
- [pages/landing/LandingPage.kt](pages/landing/LandingPage.kt)
- http://127.0.0.1:5555/

A page with one simple block.
- [pages/page_with_block/ThePage.kt](pages/page_with_block/ThePage.kt)
- http://127.0.0.1:5555/page-with-block?user-id=123222

A page with three blocks and a page route. 
Two of the blocks observe one block. 
This means if the observed block changes, the observing blocks update themselves.
- [pages/page_with_blocks_and_page_api_route/WelcomePage.kt](pages/page_with_blocks_and_page_api_route/WelcomePage.kt)
- http://127.0.0.1:5555/page-with-blocks-and-page-api-route?user-id=123222


## API Routes

A simple API route.
- [api/UserPets.kt](api/UserPets.kt)
- http://127.0.0.1:5555/api/user-pets

An API route with one parameter.
- [api/UserInformationEndpoint.kt](api/UserInformationEndpoint.kt)
- http://127.0.0.1:5555/api/user-information-endpoint?user-id=123

An API route that throws an error.
- [api/ApiRouteThatThrowsError.kt](api/ApiRouteThatThrowsError.kt)
- http://127.0.0.1:5555/api/api-route-that-throws-error