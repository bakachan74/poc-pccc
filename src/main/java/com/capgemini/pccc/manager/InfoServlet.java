package com.capgemini.pccc.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.pccc.util.Constants;

/**
 * @author sflacher
 */
public class InfoServlet extends AbstractHttpServlet {
	// =========================================== constants
	private final static Logger LOG = LoggerFactory.getLogger(InfoServlet.class);
	private final static String BR = "<BR/>\n";

	static {
		LOG.info("==== InfoServlet LOADED");
	}
	// =========================================== class variables

	// =========================================== instance variables

	// =========================================== class methods

	// =========================================== constructors
	/**
	 *
	 */
	public InfoServlet() {
	}

	// =========================================== public methods
	private String message;

	public void init() throws ServletException {
		// Do required initialization
		LOG.info("init() called");
		message = "Coucou c'est SEB!";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException {
		String method = "doGet()" + " -- ";
		LOG.info(method + Constants.C_LINE);
		LOG.info(method + "++++ URI is " + req.getRequestURI());

		List<String> reqInfoList = new ArrayList<String>();
		reqInfoList.add("==== contextPath : " + req.getContextPath() + BR);
		reqInfoList.add("==== queryString : " + req.getQueryString() + BR);
		reqInfoList.add("==== requestURI : " + req.getRequestURI() + BR);
		reqInfoList.add("==== requestURL : " + req.getRequestURL() + BR);

		/*
		 * Processing request parameters
		 */
		String attKey = null;
		String attVal = null;
		Enumeration<String> attNames = req.getAttributeNames();
		LOG.info(method + "AttributeNames");
		reqInfoList.add("==== AttributeNames" + BR);
		while (attNames.hasMoreElements()) {
			attKey = attNames.nextElement();
			attVal = (String) req.getAttribute(attKey);
			String keyVal = "" + attKey + " : " + attVal + "" + BR;
			reqInfoList.add(keyVal);
			LOG.info(keyVal);
		}
		reqInfoList.add(BR);

		String hName = null;
		String hVal = null;
		LOG.info(method + "HeaderNames");
		reqInfoList.add("==== HeaderNames" + BR);
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			hName = headerNames.nextElement();
			hVal = req.getHeader(hName);
			String keyVal = "" + hName + " : " + hVal + "" + BR;
			reqInfoList.add(keyVal);
			LOG.info(keyVal);
		}
		reqInfoList.add(BR);

		String[] prmValueAr = new String[10];
		LOG.info(method + "Parameters");
		reqInfoList.add("==== Parameters" + BR);
		Map<String, String[]> prmMap = req.getParameterMap();
		for (String prmKey : prmMap.keySet()) {
			prmValueAr = prmMap.get(prmKey);
			String prmValueArStr = Arrays.toString(prmValueAr);
			String keyVal = "" + prmKey + " : " + prmValueArStr + "" + BR;
			reqInfoList.add(keyVal);
			LOG.info(keyVal);
		}
		reqInfoList.add(BR);

		/*
		 * Building HttpResponse...
		 */
		String htmlContent = reqInfoList.toString();
		htmlContent = htmlContent.substring(1, htmlContent.length() - 1);
		htmlContent = htmlContent.replaceAll("\\,", "");

		respondWithHtml(response, "Info extracted from the HttpServletRequest", htmlContent, false);
	}

	public void destroy() {
		// do nothing.
	}

}
