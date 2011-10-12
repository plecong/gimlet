package gimlet.servlet

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import groovy.servlet.AbstractHttpServlet
import groovy.servlet.ServletBinding
import groovy.servlet.ServletCategory

import gimlet.GimletHandler

abstract class AbstractGimletServlet extends AbstractHttpServlet {

	void service(HttpServletRequest request, HttpServletResponse response) {
        def handler = getHandler(request)

        if (handler) {
			// Set it to HTML by default
	  		response.setContentType('text/html; charset=' + encoding);
	
	        ServletBinding binding = new GimletBinding(request, response, this.servletContext)

			use (ServletCategory) {
		  		def result = handler.execute(binding)

		  		// can potentially add a bunch of different resovlers
		  		if (result) {
		  			response.writer << result.toString()
		  		}
			}
        } else {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND)
        }
	}

	abstract GimletHandler getHandler(HttpServletRequest request);

}