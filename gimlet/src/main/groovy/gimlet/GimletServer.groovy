package gimlet

import org.mortbay.jetty.Handler
import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.FilterHolder
import org.mortbay.jetty.servlet.ServletHolder
import org.mortbay.jetty.webapp.WebAppContext

import gimlet.servlet.GimletFilter
import gimlet.servlet.GimletHandlerServlet

/**
 * Isolate all of the Jetty related stuff here. For projects where
 * Gimlet is deployed in a WAR, this unnecessary so we might want to
 * move this to another subproject so that dependencies on Gimlet don't
 * download Jetty as well
 */
class GimletServer {

    def jetty
    def webAppContext

    GimletServer(gimlet) {
        webAppContext = new WebAppContext()
        webAppContext.setContextPath("/")
        webAppContext.setWar("src/main/webapp")
        webAppContext.addFilter(new FilterHolder(new GimletFilter(gimlet)), "/*", Handler.REQUEST)
        webAppContext.addServlet(new ServletHolder(new GimletHandlerServlet()), GimletHandlerServlet.REQUEST_PATH)

        jetty = new Server(gimlet.port as Integer)
        jetty.addHandler(webAppContext)
        jetty.stopAtShutdown = true
    }

    void start() {
        jetty.start()
    }

    void stop() {
        jetty.stop()
    }
}