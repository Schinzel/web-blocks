# Page elements
A page element is a independent stand alone part of a page. 

```kotlin
class WelcomePe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}
```