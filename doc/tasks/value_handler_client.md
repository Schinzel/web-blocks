# Spec Value handler client

The purpose of this document is to describe how we build our
initial value handler client.

# Strategy

The overall strategy is build a client system that allows us to start with
some basic cases, but is thought out to extendable to allow build out to handle
more and mor cases. Examples of future support are

- New data time pickers
- Forms
- New triggers

# Data to send to server

There are three type of data

- value handler id
- value - The value to handle. For example first name
- context - The context of the value. For example user id or user id and document id

## Value handler id

We get from `data-value-handler-id`

## Context

### Simple data context value

```HTML
<input type="text"
  data-value-handler-id="firstName"
  data-context="{{userId}}"
  value="{{currentFirstName}}">
```

### Multi-value data context value

```HTML
<input type="text"
       data-value-handler-id="firstName"
       data-context-user-id="{{userId}}"
       data-context-document-id="{{documentId}}"
       value="{{currentFirstName}}">
```

If both data-context AND data-context-* exist on same element,
throw an error

## Values

At first we add support for the basic types:

```HTML
<input type="text"> → .value
<input type="email"> → .value
<input type="password"> → .value
<input type="number"> → .value
<input type="url"> → .value
<input type="tel"> → .value
<input type="search"> → .value
<input type="date"> → .value
<input type="time"> → .value
<input type="range"> → .value
<input type="hidden"> → .value
<input type="color"> → .value
```

When we come across cases we add support for:
- checkbox
- radiobutton
- custom date time picker
- forms

The absolutely most important aspect is to have
a strategy that allows as to incrementally add support
for new types of elements and that we do not paint our selves into a corner.

### File uploads

We might never build support for this. But it might be possible and we
should probably take a page out of HTMX's book on how to build this.

### How users can add support for values not supported

How users of value handlers adds support for new or custom elements that are not supported by the value handler system/library.
For example a new date time picker.

```HTML
<!-- Custom component with user-defined extractor -->
<input data-value-handler-id="customDate"
data-context="123"
data-value-extractor="myCustomDateExtractor">

<script>
// User CHOOSES to write this for their custom component
function myCustomDateExtractor(element) {
    return element._flatpickr.selectedDates[0]?.toISOString()
}
</script>
```

## New events

I am pretty sure that we need to add support for more events
in the future. We will do that by adding a new attribute. Details TBD.
But for example:

```HTML
<input data-value-handler-id="name"
       data-context="123"
       data-trigger="blur">
<!-- or -->
<input data-value-handler-id="name"
       data-context="123"
       data-trigger="input,blur">
```

# To solve

- What to return; JSON or HTMl
