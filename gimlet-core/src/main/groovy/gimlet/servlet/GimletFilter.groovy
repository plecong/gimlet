package gimlet.servlet

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletResponse
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import gimlet.Gimlet

class GimletFilter implements Filter {

	public static final String HANDLER_KEY = "gimlet.GimletHandler"

	private Gimlet gimlet

	/**
	 * No-arg constructor for when it's configured in web.xml
	 */
	GimletFilter() { }

	/**
	 * Constructor that takes a Gimlet reference for when it's configured via the
	 * embedded server
	 */
	GimletFilter(Gimlet gimlet) {
		this.gimlet = gimlet
	}

   void init(FilterConfig filterConfig) {
        def routesFileLocation = filterConfig.getInitParameter("routes.location") ?: "WEB-INF/gimlet/gimlet.groovy"
        def routesFile = new File(routesFileLocation)

        if (routesFile.exists()) {
        	println "Loading routes from ${routesFile.absolutePath}"
        	gimlet = new Gimlet(routesFile: routesFile)
        	gimlet.loadRoutes()
		} else if (gimlet == null) {
			// perhaps a log warn once we configure a logger
	        throw new ServletException("No configured routes")
	    }
    }	

    /**
     * Forward or redirects requests to another URL if a matching route is defined.
     * Otherwise, the normal filter chain and routing applies.
     */
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest)servletRequest
        HttpServletResponse response = (HttpServletResponse)servletResponse

		def handler = gimlet?.findHandler(request.method, request.requestURI)

		if (handler) {
			// handler is found, so we set it into the Request and forward to 
			// the GimletHandlerServlet to execute the handler

			request.setAttribute(HANDLER_KEY, handler)
			request.getRequestDispatcher(GimletHandlerServlet.REQUEST_PATH).forward(request, response)
		} else {
			filterChain.doFilter servletRequest, servletResponse
		}
    }

    void destroy() { }

}