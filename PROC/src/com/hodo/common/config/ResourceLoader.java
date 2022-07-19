/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ResourceLoader {

	public static final int LOCATION_TYPE_XML = 0;

	public static final int LOCATION_TYPE_PROPERTIES = 1;

	private static final String DELIMITER = "|";

	protected static final String CONFIG_FILE = "ans-config.xml";

	private static final String DEFAULT_LOCATION_NAME = "DEFAULT";

	protected static String configFilePath = ".";

	protected static String webappPath = "./WebRoot";

	private volatile Map resSettingCache = null;

	private volatile Map configCache = null;

	private static ResourceLoader instance = null;

	private static boolean isNotify = true;

	static {
		instance = new ResourceLoader();

		try {
			configFilePath = instance.getClassPath();

			if (!configFilePath.endsWith("/")) {
				configFilePath += "/";
			}

			webappPath = configFilePath;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ResourceLoader() {
		resSettingCache = new Hashtable();
		configCache = new Hashtable();

	}

	public static ResourceLoader getInstance() {
		return instance;
	}

	public static String getInstallPath() {
		getInstance();
		return configFilePath;
	}

	public static String getWebAppPath() {
		getInstance();
		return webappPath;
	}

	public static String getConfigFileName(String target) throws Exception {
		return getConfigFileName(target, null);
	}

	public static String getConfigFileName(String target, String locationName) throws Exception {

		getInstance().getConfig(target, locationName);

		ResourceConfig resSetting = (ResourceConfig) getInstance().resSettingCache.get(target);
		LocationConfig locationConfig = null;

		if (locationName == null || "".equals(locationName)) {
			locationConfig = resSetting.getLocation(DEFAULT_LOCATION_NAME);
		} else {
			locationConfig = resSetting.getLocation(locationName);
		}

		File file = new File(locationConfig.getURI());

		if (!file.exists()) {
			file = new File(webappPath, locationConfig.getURI());

		}

		return file.getAbsolutePath();
	}

	public static void setNotify(boolean notify) {
		isNotify = notify;
	}

	public void init() throws Exception {
		this.init(null);
	}

	public synchronized void init(String configName) throws Exception {

		Document document = LoaderUtil.loadXMLtoDOM(webappPath + CONFIG_FILE);

		NodeList configList = document.getElementsByTagName("resource-config");

		for (int i = 0; i < configList.getLength(); i++) {

			ResourceConfig resourceConfig = new ResourceConfig();

			Node node = configList.item(i);
			NodeList childList = node.getChildNodes();

			for (int j = 0; j < childList.getLength(); j++) {
				Node child = childList.item(j);

				if (child.getNodeType() == Node.ELEMENT_NODE) {
					String nodeName = child.getNodeName();
					String text = null;
					if (child.getFirstChild() != null) {
						text = child.getFirstChild().getNodeValue();
					}

					if ("name".equals(nodeName)) {

						resourceConfig.setName(text);

					} else if ("handlerClass".equals(nodeName)) {
						resourceConfig.setHandlerClass(Class.forName(text));

					} else if ("location-type".equals(nodeName)) {
						LocationConfig locationConfig = new LocationConfig();

						NamedNodeMap attrs = child.getAttributes();

						if (attrs.getNamedItem("name") != null) {
							text = attrs.getNamedItem("name").getNodeValue();

							if (text == null || "".equals(text)) {
								text = DEFAULT_LOCATION_NAME;
							}
						} else {
							text = DEFAULT_LOCATION_NAME;
						}

						locationConfig.setLocationName(text);

						text = attrs.getNamedItem("id").getNodeValue();

						if ("xml".equals(text)) {
							locationConfig.setLocationType(LOCATION_TYPE_XML);
						} else if ("properties".equals(text)) {
							locationConfig.setLocationType(LOCATION_TYPE_PROPERTIES);
						}

						text = attrs.getNamedItem("uri").getNodeValue();

						locationConfig.setURI(text);

						resourceConfig.addLocation(locationConfig);
					}
				}
			}

			if (configName == null || configName.equals(resourceConfig.getName())) {
				resSettingCache.put(resourceConfig.getName(), resourceConfig);
			}
		}
	}

	public synchronized Object getConfig(String target) throws Exception {
		return getConfig(target, null);
	}

	public synchronized Object getConfig(String target, String locationName) throws Exception {
		Object ret = null;

		if (locationName == null || "".equals(locationName)) {
			locationName = DEFAULT_LOCATION_NAME;
		}

		ret = configCache.get(getConfigKey(target, locationName));

		if (ret != null) {
			return ret;
		}

		if (resSettingCache.size() == 0) {
			init();
		}

		ResourceConfig resSetting = (ResourceConfig) resSettingCache.get(target);

		if (resSetting == null) {
			throw new IllegalArgumentException("config.xml" + target);
		}

		ret = getResource(resSetting, locationName);
		if (ret != null) {
			configCache.put(getConfigKey(target, locationName), ret);
		}

		return ret;
	}

	private Object getResource(ResourceConfig config, String locationName) throws Exception {
		Object ret = null;

		LocationConfig locationConfig = config.getLocation(locationName);

		if (locationConfig == null) {
			return null;
		}

		CreateHandler createHandler = (CreateHandler) config.getHandlerClass().getConstructor(null).newInstance(null);

		File file = null;

		switch (locationConfig.getLocationType()) {
		case LOCATION_TYPE_XML:
			file = new File(locationConfig.getURI());

			if (!file.exists()) {
				file = new File(webappPath, locationConfig.getURI());

			}

			if (createHandler.isDomSupport()) {

				Document document = LoaderUtil.loadXMLtoDOM(file.getAbsolutePath());

				ret = createHandler.create(document);

			} else if (createHandler.isDigesterSupport()) {
				InputStream inputStream = new FileInputStream(file.getAbsolutePath());
				ret = createHandler.create(inputStream);
			} else {
				throw new IllegalArgumentException("1212");
			}

			break;
		case LOCATION_TYPE_PROPERTIES:
			if (createHandler.isPropertiesSupport()) {
				ret = createHandler.create(locationConfig.getURI());
			} else {
				throw new IllegalArgumentException("11212");
			}

			break;
		default:
			break;
		}

		return ret;
	}

	private String getConfigKey(String configName, String locationName) throws Exception {

		if (locationName == null || "".equals(locationName)) {
			locationName = DEFAULT_LOCATION_NAME;
		}

		return configName + DELIMITER + locationName;
	}

	private class ResourceConfig {
		private String name = null;

		private Class handlerClass = null;

		private Map locationMap = new HashMap();

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		public void setHandlerClass(Class handlerClass) {
			this.handlerClass = handlerClass;
		}

		public Class getHandlerClass() {
			return handlerClass;
		}

		public void addLocation(LocationConfig config) {
			locationMap.put(config.getLocationName(), config);
		}

		public LocationConfig getLocation(String locationName) {
			return (LocationConfig) locationMap.get(locationName);
		}

	}

	private class LocationConfig {

		private String locationName = null;

		private int locationType = 0;

		private String uri = null;

		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}

		public String getLocationName() {
			return locationName;
		}

		public void setLocationType(int locationType) {
			this.locationType = locationType;
		}

		public int getLocationType() {
			return locationType;
		}

		public void setURI(String uriParam) {
			this.uri = uriParam;
		}

		public String getURI() {
			return uri;
		}
	}

	private String getClassPath() {
		String strClassName = getClass().getName();
		String strPackageName = "";
		if (getClass().getPackage() != null) {
			strPackageName = getClass().getPackage().getName();
		}

		String strClassFileName = "";
		if (!"".equals(strPackageName)) {
			strClassFileName = strClassName.substring(strPackageName.length() + 1, strClassName.length());
		} else {
			strClassFileName = strClassName;
		}

		URL url = null;
		url = getClass().getResource(strClassFileName + ".class");
		String strURL = url.toString();
		strURL = strURL.substring(strURL.indexOf('/') + 1, strURL.lastIndexOf('/'));

		if (strURL.indexOf("WEB-INF") != -1) {
			strURL = strURL.substring(0, strURL.indexOf("WEB-INF")) + "WEB-INF/";
		} else {
			strURL = strURL.substring(0, strURL.toLowerCase().indexOf("lib")) + "config/";
		}

		return strURL;
	}
}
