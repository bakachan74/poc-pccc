package com.capgemini.pccc.manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sflacher
 */
public class RequestInfoServlet extends AbstractServlet {
	// =========================================== constants
	private final static Logger LOG = LoggerFactory.getLogger(RequestInfoServlet.class);
	private final static String C_LINE = "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC";
	private final static String CR = "<br/>";

	static {
		LOG.info("==== RequestInfoServlet LOADED");
	}
	// =========================================== class variables

	// =========================================== instance variables

	// =========================================== class methods

	// =========================================== constructors
	/**
	 * @param uri
	 */
	public RequestInfoServlet() {
	}

	// =========================================== public methods
	public void init() throws ServletException {
		// Do required initialization
		LOG.info("init() called");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException {
		String method = "doGet()" + " -- ";
		String infoText = getReqInfo(req);
		LOG.info(C_LINE);
		LOG.info(method + infoText);
		LOG.info(C_LINE);

		/*
		 * Building HttpResponse...
		 */
		response.setContentType("text/html");

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(infoText);
		} catch (IOException e) {
			LOG.error(method + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public void destroy() {
		// do nothing.
	}

}
