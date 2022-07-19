/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.util;


/**
 * 
 * @author Zhao GuoWei
 * @since ANS 1.1
 * @version Revision: Date: 2008/10/16
 */
public final class TextareaUtil {

	/**
	     * Html转换为TextArea文本
	     * @return
	     */
	     public static String HtmlToText(String str) {
	         if (str == null) {
	             return "";
	         }else if (str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<br />", "\n");
		str = str.replaceAll("<br />", "\r");
		return str;
	}

	/**
	 * TextArea文本转换为Html:写入数据库时使用
	 */
	public static String Text2Html(String str) {
		if (str == null) {
			return "";
		} else if (str.length() == 0) {
			return "";
		}
		str = str.replaceAll("\r\n", "<br />");
		str = str.replaceAll("\n", "<br />");
		return str;
   }
}