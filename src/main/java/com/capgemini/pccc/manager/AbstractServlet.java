package com.capgemini.pccc.manager;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sflacher
 */
public class AbstractServlet extends HttpServlet {
	// =========================================== constants
	private final static Logger LOG = LoggerFactory.getLogger(AbstractServlet.class);
	private final static String C_LINE = "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC";
	private final static String CR = "<br/>";

	static {
		LOG.info("==== AbstractServlet LOADED");
	}
	// =========================================== class variables

	// =========================================== instance variables

	// =========================================== class methods

	// =========================================== constructors
	/**
	 * @param uri
	 */
	public AbstractServlet() {
	}

	// =========================================== public methods
	public void init() throws ServletException {
		// Do required initialization
		LOG.info("init() called");
	}

	public String getReqInfo(HttpServletRequest req) {
		StringBuffer sb = new StringBuffer(1000);
		/*
		 * Processing request parameters
		 */
		sb.append(buildBlockHeader("AttributeNames"));
		LOG.info("AttributeNames");
		String attKey = null;
		String attVal = null;
		Enumeration<String> attNames = req.getAttributeNames();
		while (attNames.hasMoreElements()) {
			attKey = attNames.nextElement();
			attVal = (String) req.getAttribute(attKey);
			LOG.info("[" + attKey + "] - [" + attVal + "]");
			sb.append("[" + attKey + "] - [" + attVal + "]" + CR);
		}

		sb.append(buildBlockHeader("HeaderNames"));
		LOG.info("HeaderNames");
		String hName = null;
		String hVal = null;
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			hName = headerNames.nextElement();
			hVal = req.getHeader(hName);
			LOG.info("[" + hName + "] - [" + hVal + "]");
			sb.append("[" + hName + "] - [" + hVal + "]" + CR);
		}

		sb.append(buildBlockHeader("Parameters"));
		LOG.info("Parameters");
		Map<String, String[]> prmMap = req.getParameterMap();
		for (String prmKey : prmMap.keySet()) {
			String[] prmValueAr = prmMap.get(prmKey);
			LOG.info("[" + prmKey + "] - [" + renderStringAr(prmValueAr) + "]");
			sb.append("[" + prmKey + "] - [" + renderStringAr(prmValueAr) + "]");
		}

		return sb.toString();
	}

	/**
	 * @param text
	 * @return
	 */
	private Object buildBlockHeader(String text) {
		return "<H4>" + text + "</H4>";
	}

	/**
	 * @param prmAr
	 * @return
	 */
	private String renderStringAr(String[] prmAr) {
		String text = "";
		for (String prm : prmAr)
			text += prm + "--";

		if (prmAr.length != 0)
			text = text.substring(0, text.length() - 2);

		return text;
	}

	public void destroy() {
		// do nothing.
	}

}
