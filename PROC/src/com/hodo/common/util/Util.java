/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;


public class Util {

	public static final String CHARSET_NAME = "MS932";

	protected Util() {
	}
	/**
	 * 判断是否大数字
	 */
	public static boolean isNumeric(String target) {
		try {
			new BigDecimal(target);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	/**
	 * 判断是否半角
	 */
	public static boolean isHalf(String target) {
		boolean isHalf = true;
		try {
			byte[] bytes = target.getBytes(CHARSET_NAME);
			int originalLength = target.length();
			if (originalLength != bytes.length) {
				isHalf = false;
			}
		} catch (UnsupportedEncodingException e) {
			isHalf = false;
		}
		return isHalf;
	}
	/**
	 * 判断是否全角
	 */
	public static boolean isFull(String target) {
		boolean isFull = true;
		try {
			byte[] bytes = target.getBytes(CHARSET_NAME);
			int originalLength = target.length() * 2;
			if (originalLength != bytes.length) {
				isFull = false;
			}
		} catch (UnsupportedEncodingException e) {
			isFull = false;
		}
		return isFull;
	}
	/**
	 * 判断对象是否为空-常用
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			String val = (String) obj;
			val = val.replaceAll(" ", "");
			if (val.trim().length() == 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断对象是否不为空-常用
	 */
	public static boolean isNotEmpty(Object obj) {
		if(isEmpty(obj)){
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 判断字符串内容是否一样
	 */
	public static boolean isEquals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		} else if (obj2 == null) {
			return obj1.equals(obj2);
		} else {
			return obj2.equals(obj1);
		}
	}
	/**
	 * 判断一个对象的两个值的内容是否相等
	 */
	public static boolean isEquals(Object obj, String name1, String name2) throws Exception {
		Object obj1 = PropertyUtils.getProperty(obj, name1);
		Object obj2 = PropertyUtils.getProperty(obj, name2);
		return isEquals(obj1, obj2);
	}
	/**
	 * 字符串格式化成Date
	 * 
	 * @param strdate
	 * 
	 * @throws AppException
	 * 
	 * @return Date
	 */
	public static Date getFormatDate(String strdate) 
	{
		if(strdate==null||strdate.equals(""))
		return null;
			try 
			{
				//log.info("strdate===:"+strdate.length());
				String pattern = "";
				if(strdate.length()>18)
				{
					pattern = "yyyy-MM-dd kk:mm:ss";
				}
				else
				{
					pattern = "yyyy-MM-dd";
				}
				SimpleDateFormat formater = new SimpleDateFormat(pattern);				
				return formater.parse(strdate);
			} catch (ParseException error)
			{
				return null;
			}
	}
	/**
	 * 将得到的日期转换成相应的 String 的类型
	 * 
	 * @param date	需要转化的日期
	 * @param formate	转换的格式
	 * 
	 * @throws Exception
	 * 
	 * @return	String	返回转换后的日期字符串
	 */
	public static String getFormatDate(Date date,String formate) 
	{
	  try
		{
			//"yyyy-MM-dd"
			if(date==null || date.equals("") || formate==null || formate.equals("")) return "";
			SimpleDateFormat formatter;
		    formatter = new SimpleDateFormat(formate);
		    return formatter.format(date);
		}
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
			return "";
		}
	}
	/**
	 * 判断对象是否是日期
	 * 
	 */
	public static boolean isDate(Object value) {
		boolean isDate = false;
		try {
			if (value != null) {
				if (value instanceof String) {
					Date tmpDate = DateUtil.parse((String) value);
					if (!isEmpty(tmpDate)) {
						isDate = true;
					} else {
						isDate = false;
					}
				} else if (value instanceof Date) {
					isDate = true;
				}
			}
			return isDate;
		} catch (Exception ex) {
			return isDate;
		}
	}
	/**
	 * 去除前后逗号，以及两个连续的逗号变成一个
	 * 
	 */
	public static String delDou(String s) {
		String mainImg2 =s;
		if(!Util.isEmpty(s)){
			if(mainImg2.startsWith(",")){
				mainImg2= mainImg2.replaceFirst(",", "");
			}
			if(mainImg2.endsWith(",")){
				mainImg2= mainImg2.substring(0, mainImg2.length()-1);
			}
			mainImg2= mainImg2.replace(",,", ",");
		}
		
		return mainImg2;
	}
	
}
