package gimlet

class GimletBaseScript extends Script implements GimletConfig {

    boolean servable = true

    List<Route> routes = []

    Closure configClosure

    /** contributed binding variables */
    Map bindingVariables = [:]

    // part of the config
    Closure before
    Closure after
    Closure enrich
    List categories = []
    boolean reload = true

    // to fulfill Script
    def run() { }

    // various DSL methods
	def all    (String route, Closure closure) { handle route, closure, HttpMethod.ALL }
    def get    (String route, Closure closure) { handle route, closure, HttpMethod.GET }
    def post   (String route, Closure closure) { handle route, closure, HttpMethod.POST }
    def put    (String route, Closure closure) { handle route, closure, HttpMethod.PUT }
    def delete (String route, Closure closure) { handle route, closure, HttpMethod.DELETE }

    def configure(Closure closure) {
        configClosure = closure
    }

    def before(Closure closure) {
        before = closure        
    }

    def after(Closure closure) {
        after = closure
    }

    def categories(Class... cats) {
        categories = cats as List
    }

    def categories(List cats) {
        categories = cats
    }

    def enrich(Closure closure) {
        enrich = closure
    }

    def reload(boolean r) {
        reload = r
    }

	protected handle(String route, Closure closure, HttpMethod method) {
        routes << new Route(route, closure, method)
    } 

    /**
     * This method is added to the end of the script method after the routes
     * have been loaded using the AST. This is so that a simple script that
     * doesn't have anything in it except route definitions will serve itself
     * when run without the need for an explicit "Gimlet.serve this"
     */
    def serve() {
        if (servable) {
            Gimlet.configure(this).serve();
        }
    }

    def printRoutes() {
        println "Routes: ${routes}"
    }

}