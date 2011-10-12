package gimlet

interface GimletConfig {
	Closure getBefore()
	Closure getAfter()
	List<Route> getRoutes()

	Closure getEnrich()
	List<Class> getCategories()

	boolean isReload()
}