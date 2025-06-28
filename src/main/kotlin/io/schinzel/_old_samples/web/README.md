## Sample

Below directories are relative to this README file.

Start the main file `MyWebApp.kt`

Returns the page `ThePage.kt` that resides in the directory `pages/my_page/` folder
- http://127.0.0.1:5555/my-page

Returns the page `MyOtherPage.kt` that resides in the directory `pages/my_dir/my_page/` folder
- http://127.0.0.1:5555/my-dir/any-page

Calls the endpoint `MyPersonEndpoint.kt` that resides in the directory `api/my_dir/` folder
- http://127.0.0.1:5555/api/my-dir/my-person

Calls the page endpoint `SavePersonNamePageEndpoint.kt` that resides in the directory `pages/my_page/`
- http://127.0.0.1:5555/page-api/my-page/save-person-name?user-id=123&first-name=John
