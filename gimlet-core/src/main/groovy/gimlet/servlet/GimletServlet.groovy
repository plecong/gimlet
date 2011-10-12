package gimlet.servlet

import javax.servlet.http.HttpServletRequest

import gimlet.Gimlet
import gimlet.GimletHandler

/**
 * Standalone servlet for executing Gimlet handlers without the 
 * help of the GimletFilter.
 */
class GimletServlet extends AbstractGimletServlet {
	
	private Gimlet gimlet = null

	GimletServlet(gimlet) {
		this.gimlet = gimlet
	}

	GimletHandler getHandler(HttpServletRequest request) {
		return gimlet?.findHandler(request.method, request.requestURI)
	}

}