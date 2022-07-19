/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.config;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class LoaderUtil {
	private static Log log = LogFactory.getLog(LoaderUtil.class);
	
	private LoaderUtil() {
	}
	
	public static Document loadXMLtoDOM(String file) throws Exception {
		return loadXMLtoDOM(file, true);
	}

	public static Document loadXMLtoDOM(String file, boolean validating) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		
		DocumentBuilder domBuilder = factory.newDocumentBuilder();
		
		DefaultHandlerEx errorHandler = new DefaultHandlerEx();
		errorHandler.reset();
		
		domBuilder.setErrorHandler(errorHandler);
		
		Document document = domBuilder.parse(new File(file));
		
		int errCount = errorHandler.getWarningCount()
						+ errorHandler.getErrorCount()
						+ errorHandler.getFatalErrorCount();
		
		if (errCount > 0) {
			throw new DOMException((short) 0, "Warning="
					+ errorHandler.getWarningCount() + "\n" + "Error="
					+ errorHandler.getErrorCount() + "\n" + "FatalError="
					+ errorHandler.getFatalErrorCount());
		}
		
		return document;
	}

	static class DefaultHandlerEx extends DefaultHandler {
		private int warningCount = 0;
		
		private int errorCount = 0;
		
		private int fatalErrorCount = 0;
		
		public void warning(SAXParseException exception) throws SAXException {
			
			log.warn(exception.toString());
			
			warningCount++;
			super.warning(exception);
		}
		
		public void error(SAXParseException exception) throws SAXException {
			
			log.error(exception.toString());
			
			errorCount++;
			super.error(exception);
		}
		
		public void fatalError(SAXParseException exception) throws SAXException {
			
			log.fatal(exception.toString());
			
			fatalErrorCount++;
			super.fatalError(exception);
		}
		public int getWarningCount() {
			return warningCount;
		}
		
		public int getErrorCount() {
			return errorCount;
		}
		public int getFatalErrorCount() {
			return fatalErrorCount;
		}
		
		public void reset() throws SAXException {
			warningCount = 0;
			errorCount = 0;
			fatalErrorCount = 0;
		}
	}
}