package gimlet.servlet

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletContext

import groovy.servlet.ServletBinding

class GimletBinding extends ServletBinding {

	GimletBinding(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		super(request, response, context)
	}

	void println(message) {
		out.println(message);
	}
}