# Value handlers

One of the most common code patterns in a web
app is to write code that;
1) take the data the user filled out and send it to the server
2) create and endpoint that receives the data
3) validate the data sent to the server
4) persist the data sent to the server

The purpose of Value Handlers is to facilitate this.

## Building blocks

### Value Handlers
Value handlers handles the data sent from the server and must implement
the [IValueHandler](../../src/main/kotlin/io/schinzel/web_blocks/component/value_handler/IValueHandler.kt) interface.

### Saving Value Handlers
The most common value handler action is to validate and persist data.
To facilitate this one can implement
the [ISavingValueHandler](../../src/main/kotlin/io/schinzel/web_blocks/component/value_handler/ISavingValueHandler.kt) interface.

### Value Handler Registry
The value handlers are registered with the Value Handler Registry with a unique name

```kotlin
ValueHandlerRegistry.instance
  .register("My_Unique_Value_Handler_Name", MyValueHandler::class.java)
```

As the most common action is to validate and persist data, this convince function exists
that accepts lambdas.
```kotlin
ValueHandlerRegistry.instance.registerSavingHandler("password",
    validateFunc = { password: String ->
        val errors = mutableListOf<String>()

        if (password.length < 8) errors.add("Password must be at least 8 characters")
        if (!password.any { it.isDigit() }) errors.add("Password must contain a number")
        if (!password.any { it.isUpperCase() }) errors.add("Password must contain uppercase letter")

        if (errors.isEmpty()) {
            ValueHandlerResponse(ValueHandlerStatus.SUCCESS)
        } else {
            ValueHandlerResponse(ValueHandlerStatus.VALIDATION_ERROR, errors)
        }
    },
    saveFunc = { password: String ->
        userDao.updatePassword(currentUserId, password.hashWithSalt())
        ValueHandlerResponse(ValueHandlerStatus.SUCCESS)
    }
)
```


### Endpoint
All value handler data sent from the client is sent to the same endpoint with the unique name
user to register the value handler.
The value handler is found using the Value Handler Registry and the data is sent to the
value handler for processing.


### HTML element
TODO: DOCUMENT HOW THESE WORK AFTER IMPLEMENTATION








## Data Saver Value handler

If the validation fails:
1) The `ValueHandlerResponse.status` should be `ValueHandlerStatus.VALIDATION_ERROR`
2) It is recommended that `ValueHandlerResponse.errorMessages` contains all validation errors and not just the first so that:
   1) The user get full information of what fails.
   2) Each validation error is a separate element in the list as to facilitate a better presentation client side.
