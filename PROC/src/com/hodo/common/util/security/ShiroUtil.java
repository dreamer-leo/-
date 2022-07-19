package com.hodo.common.util.security;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.hodo.bean.Login;
import com.hodo.common.base.Const;
import com.hodo.common.base.SessionInfo;

/**
 * 封装shiro用对象获取
 * 
 */
public class ShiroUtil {
	/**
	 * 获取当前对象的拷贝
	 * 
	 * @return
	 */
	public static Login getCurrentUser() {
		Login customer = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if (null != session) {
			Object obj = session.getAttribute(Const.SESSION_USER);
			if (null != obj && obj instanceof Login) {
				try {
					/**
					 * 复制一份对象，防止被错误操作
					 */
					customer = (Login) BeanUtils.cloneBean((Login) obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return customer;
	}
	public static SessionInfo getCurrentInfo() {
		SessionInfo sessionInfo = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if (null != session) {
			Object obj = session.getAttribute(Const.SESSION_INFO);
			if (null != obj && obj instanceof SessionInfo) {
				try {
					/**
					 * 复制一份对象，防止被错误操作
					 */
					sessionInfo = (SessionInfo) BeanUtils.cloneBean((SessionInfo) obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sessionInfo;
	}
	/**
	 * 获取当前真实的对象，可以进行操作实体
	 * 
	 * @return
	 */
	public static Login getRealCurrentUser() {
		Login customer = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if (null != session) {
			Object obj = session.getAttribute(Const.SESSION_USER);
			if (null != obj && obj instanceof Login) {
				try {
					/**
					 * 不复制一份对象，防止被错误操作
					 */
					customer = (Login) obj;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return customer;
	}
}
