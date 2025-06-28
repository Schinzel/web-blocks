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

A page with one simple page element.
- [pages/page_with_page_element/ThePage.kt](pages/page_with_page_element/ThePage.kt)
- http://127.0.0.1:5555/page-with-page-element?user-id=123222

A page with three page elements and a page route. 
Two of the page elements observe one page element. 
This means if the observed page element changes, the observing page elements update themselves.
- [pages/page_with_page_elements_and_page_api_route/Page.kt](pages/page_with_page_elements_and_page_api_route/Page.kt)
- http://127.0.0.1:5555/page-with-page-elements-and-page-api-route?user-id=123222


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