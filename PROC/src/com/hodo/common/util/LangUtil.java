package com.hodo.common.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;


/**
 * 国际化工具类
 */
public final class LangUtil {

	 /**
     * 从国际化资源配置文件中根据key获取value  方法一
     * @param request
     * @param key
     * @return
     */
    public static String getMessage(HttpServletRequest request, String key){
        Locale currentLocale = RequestContextUtils.getLocale(request);
        String lang = currentLocale.getLanguage();
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+lang, currentLocale);
        return bundle.getString(key);
    }
    /**
     * 从国际化资源配置文件中根据key获取value  方法二
     * @param request
     * @param key
     * @return
     */
    public static String getMessage2(HttpServletRequest request, String key){
        RequestContext requestContext = new RequestContext(request);
        String value = requestContext.getMessage(key);
        return value;
    }
}