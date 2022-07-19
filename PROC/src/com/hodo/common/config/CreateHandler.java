/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.config;

import java.io.InputStream;
import java.sql.Connection;

import org.w3c.dom.Document;

public interface CreateHandler {

	String getTag();

	boolean isDomSupport();

	Object create(Document document) throws Exception;

	boolean isDigesterSupport();

	Object create(InputStream inputStream) throws Exception;

	boolean isPropertiesSupport();

	Object create(String path) throws Exception;

	boolean isDBSupport();

	Object create(Connection conn, Object[] param) throws Exception;
}