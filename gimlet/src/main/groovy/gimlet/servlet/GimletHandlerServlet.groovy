package gimlet.servlet

import javax.servlet.http.HttpServletRequest

import gimlet.GimletHandler

class GimletHandlerServlet extends AbstractGimletServlet {

	public static final String REQUEST_PATH = "/gimlet.GimletHandlerServlet"

	GimletHandler getHandler(HttpServletRequest request) {
		return request.getAttribute(GimletFilter.HANDLER_KEY)
	}

}