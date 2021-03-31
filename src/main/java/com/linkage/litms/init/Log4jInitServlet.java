package com.linkage.litms.init;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.xml.DOMConfigurator;

public class Log4jInitServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2542897545066831112L;

	/**
	 * Constructor of the object.
	 */
	public Log4jInitServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");
		if (file != null) {
			DOMConfigurator.configureAndWatch(prefix + file);
			//PropertyConfigurator.configure(prefix + file);
		}
	}

}
