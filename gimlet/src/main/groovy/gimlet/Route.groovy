package gimlet

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Representation of a route URL mapping. Largely copied from Gaelyk.
 */
class Route {

    /** The route pattern */
    String route

    /** The destination pattern when the route is matched, can be a String or a RoutingRule */
    def destination

    /** The HTTP method used to reach that route */
    HttpMethod method

    /* The list of variables in the route */
    private List variables

    /* The real regex pattern used for matching URIs */
    private Pattern regex

    Route(String route, /* String or Closure */ destination, HttpMethod method = HttpMethod.ALL) {
        this.route = route
        this.destination = destination 
        this.method = method

        // extract the path variables from the route
        this.variables = extractParameters(route)

        // create a regular expression out of the route string
        this.regex = Pattern.compile(transformRouteIntoRegex(route))
    }

    /**
     * Extract a list of parameters in the route URI.
     */
    static List<String> extractParameters(String route) {
        route.findAll(/@\w*/)
    }

    /**
     * Transform a route pattern into a proper regex pattern.
     */
    static String transformRouteIntoRegex(String route) {
        route.replaceAll('\\.', '\\\\.')
                .replaceAll('\\*\\*', '(?:.+\\/?){0,}')
                .replaceAll('\\*', '.+')
                .replaceAll('@\\w+', '(.+)')
    }

    /**
     * Checks whether a URI matches a route.
     *
     * @return a map with a 'matches' boolean key telling whether the route is matched
     * and a variables key containing a map of the variable key and matched value.
     */
    def forUri(String uri) {
        Matcher matcher = regex.matcher(uri)
           
        if (matcher.matches()) {
            def variableMap = variables ?
                // a map like ['year': '2009', 'month': '11']
                variables.inject([:]) { map, variable ->
                    map[variable.substring(1)] = matcher[0][map.size()+1]
                    return map
                } : [:] // an empty variables map if no variables were present  

            return new GimletHandler(closure: destination.clone(), splat: variableMap)
        }

        return null
    }
}
