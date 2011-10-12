Gimlet
======

Gimlet is another [Sinatra](https://github.com/sinatra/sinatra/) inspired DSL for creating small web apps in [Groovy](http://groovy.codehaus.org/).


Why Another Micro Web Framework?
--------------------------------

Originally this was a project to play around with [Groovy AST Transforms](http://groovy.codehaus.org/Compile-time+Metaprogramming+-+AST+Transformations), using them to see what I could do with re-arranging the code to make the groovy code as close to [Sinatra](https://github.com/sinatra/sinatra/) as possible. Using AST Transforms means that we don't need any special runner scripts (like with [Ratpack](https://github.com/bleedingwolf/Ratpack)) and means we can make the route definitions look like a real DSL as opposed to annotated methods (like with [Grafitti](https://github.com/webdevwilson/graffiti)). 

Also, Gimlet was being developed to be used in a [Google App Engine](http://code.google.com/appengine/) project so I had originally taken a look at [Gaelyk](https://github.com/glaforge/gaelyk) however I was unhappy with the need to create multiple groovy files for each route and configure a separate routing file. Ideally everything could be served from one file.


tl;dr - Goals:
--------------

- Should look like a DSL and leverage Groovy 1.8 features to that effect (e.g. no annotations for routing)
- Must be runnable from a simple Groovy script with a [Grab]<http://groovy.codehaus.org/Grapes+and+grab()> annotation (see examples below)
- Must be runnable inside a WAR to be deployed in Google App Engine
- Should have a plugin structure to add features from Gaelyk


References & Inspiration
------------------------

- [Sinatra](https://github.com/sinatra/sinatra/)
- [Ratpack](https://github.com/bleedingwolf/Ratpack)
- [Grafitti](https://github.com/webdevwilson/graffiti)
- [Gaelyk](https://github.com/glaforge/gaelyk)


Examples
------------------------

### 1. Basic Script

This script should run (given that Gimlet is in an appropriate Maven repo) without any need to download anything.

```groovy
@Grab('com.gimlet:gimlet:0.1')
@GimletScript
import gimlet.*

get('/') { 
	'Hello World'
}
```

### 2. Embedded in a WAR

Using a project template that contains the correct `web.xml` the basic script example can be reduced to:

```groovy
get('/') { 
	'Hello World'
}
```


TODO
------------------------

1. Upgrade to Groovy 1.8
1. Plugin Structure
1. Create Maven repo on Github
1. Move server to a separate package
1. Templating?
