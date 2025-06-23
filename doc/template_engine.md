# Template Engine
- Variables can be passed to the template engine.
  In the file variables to be replaced with values have the syntax `{{variableName}}`.
- Template files can include other template files. The syntax for this is `{{include:fileName.html}}`.
    - Include files can contain include files. There is a maximum depth of 10 levels of include files, as to avoid infinite loops.

Example
```HTML
<html lang="">
<head>
    <title>Basic Page</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
</head>
<body>
        <h1>Hi {{firstName}}!</h1>
</body>
</html>
```

## Template Engine File Reader
There exists two file readers:
- The source file reader. Read files from the source folder. This means that changes to these files will be
  reflected instantly in the application without the need for a hot reload.
- JAR file reader. Read files from the JAR file. This file reader caches read files.

The file readers take two arguments:
- The file name and path
- The caller class. The files are read relative to the caller class.

The code automatically selects file reader based on if the application is run as a JAR file or not.