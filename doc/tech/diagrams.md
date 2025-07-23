# Architecture Diagrams

This document contains the architectural diagrams for the WebBlocks framework.

## Response Handler Hierarchy

```mermaid
classDiagram
    class IResponseHandler {
        <<interface>>
        The purpose of this interface it to
        generate the response for a request
    }
    
    class IApiEndpoint {
        <<ResponseHandler>>
        An endpoint in the API. I.e. an
        endpoint that is not connected to a
        page.
    }
    
    class IPage {
        <<ResponseHandler>>
        Generates the HTML and JS for a web
        page. Response for a web page
    }
    
    class IPageEndpoint {
        <<ResponseHandler>>
        Response for a page endpoint. For
        example an endpoint that saves the
        name for a person.
    }
    
    IResponseHandler <|-- IApiEndpoint : IS A
    IResponseHandler <|-- IPage : IS A
    IResponseHandler <|-- IPageEndpoint : IS A
```

## Response Handler Descriptor Hierarchy

```mermaid
classDiagram
    class IResponseHandlerDescriptor {
        <<interface>>
        The purpose of this interface it to
        provide information on a response
        handler when we do not have an
        instance. For example when setting up
        the routes we do not have instances,
        just classes.
    }
    
    class IApiEndpoint_ResponseHandler_Descriptor {
        <<ResponseHandlerDescriptor>>
    }
    
    class IPage_ResponseHandler_Descriptor {
        <<ResponseHandlerDescriptor>>
    }
    
    class IPageEndpoint_ResponseHandler_Descriptor {
        <<ResponseHandlerDescriptor>>
    }
    
    IResponseHandlerDescriptor <|-- IApiEndpoint_ResponseHandler_Descriptor : IS A
    IResponseHandlerDescriptor <|-- IPage_ResponseHandler_Descriptor : IS A
    IResponseHandlerDescriptor <|-- IPageEndpoint_ResponseHandler_Descriptor : IS A
```

## Request Handling Flow

```mermaid
flowchart TD
    A["Javalin Route<br/>Has:<br/>- HTTP method<br/>- path<br/>- request handler"] 
    A -->|HAS A| B["RequestHandler<br/>Handles:<br/>- Logging<br/>- Error handling<br/>- Create a response handler<br/>- Generates a response<br/>- Sends the response"]
    B -->|HAS A| C[ILogger]
    B -->|HAS A| D["ResponseHandlerMapping<br/>Has:<br/>- IResponseHandlerClass<br/>- constructor parameters for response handler class<br/>- path to endpoint<br/>- type of route API, Page, ...<br/><br/>Uses a ResponseHandlerDescriptor to derive path and typ"]
    
    %% Title
    E[Handling of requests]
    style E fill:none,stroke:none
    
    %% Left align text
    classDef leftAlign text-align:left
    class A,B,D leftAlign
```
