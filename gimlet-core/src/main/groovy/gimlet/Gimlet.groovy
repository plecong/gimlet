package gimlet

import org.codehaus.groovy.control.CompilerConfiguration

class Gimlet {

	GimletConfig config
	File routesFile = null

	GimletServer server = null
	int port = 8080

	private long lastRoutesFileModification = 0

	void serve() {
		println "== The Gimlet is on the bar .."
		server = new GimletServer(this)
		server.start()
		println "Listening on ${port}"
	}

    GimletHandler findHandler(String method, String uri) {
    	if (config.reload) {
    		loadRoutes()
    	}

    	def handler = config.routes.findResult({ route ->
    		(route.method == HttpMethod.ALL || route.method.toString() == method) ? 
    			route.forUri(uri) : 
    			null
	    })

	    if (handler) {
   			handler.config = config
	    }

	    handler
	}

	protected synchronized void loadRoutes() {
		if (!routesFile || !routesFile.exists()) return
		def lastModified = routesFile.lastModified()

		if (lastModified > lastRoutesFileModification) {
			println "(Re-)loading Gimlet script: ${routesFile}"

			def compilerConfig = new CompilerConfiguration(
				scriptBaseClass: GimletBaseScript.class.name,
				classpath: routesFile.parentFile.absolutePath
			)

			def gimletScript = (GimletBaseScript)new GroovyShell(GimletBaseScript.class.classLoader, new Binding(), compilerConfig).parse(routesFile)
			gimletScript.servable = false
			gimletScript.run()
			
			config = gimletScript
			lastRoutesFileModification = lastModified
		}

		assert config != null
	}

	static serve(routeClosureOrFile) {
		def gimlet = configure(routeClosureOrFile)
		gimlet.serve()
	}

	static Gimlet configure(GimletConfig config) {
		if (config instanceof GimletBaseScript) {
			def location = config.getClass().getProtectionDomain()?.getCodeSource()?.getLocation()
			
			if (location) {
				println "Configuring Script: ${config}"
				return configure(new File(location.toURI()))
			}
		}

		return new Gimlet(config: config)
	}

	/**
	 * Factory method for configuring Gimlet routes using the DSL inside of a closure
	 */
	static Gimlet configure(Closure routes) {
		def gimletScript = new GimletBaseScript()

		routes.delegate = gimletScript
		routes.call()
		
		new Gimlet(config: gimletScript)
	}

	/**
	 * Convenience method to pass in a file name as a string that is a script file 
	 * defining the routes using the DSL
	 */
	static Gimlet configure(String fileName) {
		configure(new File(fileName))
	}

	/**
	 * Factory method for configuring Gimlet using a script file that is reloaded when
	 * changed. This is what is used when the AST is performed on a script file with
	 * the @GimletScript annotation
	 */
	static Gimlet configure(File scriptFile) {
    	if (!scriptFile.exists()) {
    		throw new IllegalArgumentException("Couldn't serve non-existent file [${scriptFile}]")
    	}

    	def gimlet = new Gimlet(routesFile: scriptFile)
    	gimlet.loadRoutes()
    	gimlet
	}

}
