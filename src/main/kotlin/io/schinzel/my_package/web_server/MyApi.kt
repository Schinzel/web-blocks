package io.schinzel.my_package.web_server

import io.javalin.http.HandlerType
import io.schinzel.basicutils.RandomUtil
import se.refur.javalin.Api
import se.refur.javalin.Param
import se.refur.javalin.ParameterType

class MyApi {

    @Api(type = HandlerType.GET, path = "/api/v1/myEndpoint", accessRole = "PUBLIC")
    fun myEndpoint(): String {
        return "Hello World " + RandomUtil.getRandomString(5)
    }


    @Api(type = HandlerType.GET, path = "/api/createPerson", accessRole = "ADMIN")
    fun apiQueryEndpoint(
            @Param("name", ParameterType.QUERY) userName: String,
            @Param("age", ParameterType.QUERY) userAge: Int,
    ): String{
        return "Created person $userName that is $userAge years old"
    }


    @Api(type = HandlerType.POST, path = "/api/createPersonJsonResponse", accessRole = "ADMIN")
    fun apiQueryEndpointJsonResponse(
            @Param("personName", ParameterType.FORM) personName: String,
            @Param("personAge", ParameterType.FORM) personAge: Int,
    ): String{
        return """{"name": "$personName", "age": $personAge}"""
    }
}