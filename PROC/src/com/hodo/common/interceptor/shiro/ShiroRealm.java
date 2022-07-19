package com.hodo.common.interceptor.shiro;

import java.io.IOException;
import java.util.Properties;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.hodo.bean.Login;
import com.hodo.bean.User;
import com.hodo.common.base.Const;
import com.hodo.common.base.SessionInfo;
import com.hodo.common.util.Util;
import com.hodo.service.LoginServiceI;
import com.hodo.service.UserServiceI;

public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private LoginServiceI loginService;
	@Autowired
	private UserServiceI userService;
	/*
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		System.out.println("-------认证-------");
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
		
		String username = token.getUsername();
		String pwd = String.valueOf(token.getPassword());
		Login user = loginService.findLoginByCname(username);
		if(!Util.isEmpty(user)) {
//			//查询部门资源
//			List<DeptResource> deptResourceList = new ArrayList<DeptResource>(); ;
//			if(!Util.isEmpty(user.getDept())) {
//				deptResourceList = deptResourceService.findDeptResourceByDept(user.getDept());
//			}
//			//查询角色资源
//			List<RoleResource> roleResourceList = new ArrayList<RoleResource>();;
//			if(!Util.isEmpty(user.getRole())) {
//				roleResourceList = roleResourceService.findRoleResourceByRole(user.getRole());
//			}
//			//合并
//			if(!Util.isEmpty(deptResourceList) || !Util.isEmpty(roleResourceList)) {
//				List<Resource> resourceList = new ArrayList<Resource>();
//				List<String> resourceIds = new ArrayList<String>();
//				for (DeptResource deptResource : deptResourceList) {
//					resourceList.add(deptResource.getResource());
//				}
//				for (RoleResource roleResource : roleResourceList) {
//					resourceList.add(roleResource.getResource());
//				}
//				// 去重
//		        for (int i = 0; i < resourceList.size() - 1; i++) {
//		            for (int j = i + 1; j < resourceList.size(); j++) {
//		                if (resourceList.get(i).getCid().equals(resourceList.get(j).getCid())) {
//		                	resourceList.remove(j);
//		                }
//		            }
//		        }
//		        //简化
//		        for (Resource resource : resourceList) {
//		        	resourceIds.add(resource.getCid());
//				}
//		        //添加
//		        user.setResourceIds(resourceIds);
//			}
			
			Properties properties = new Properties();
            try {
				properties.load(ShiroRealm.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
            String saltString = properties.getProperty("salt");
            
			ByteSource salt = ByteSource.Util.bytes(saltString);//盐值
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getCpwd(), salt, this.getName());
			
			//将sessionUser sessionInfo放到ShiroSession中,以便于其它地方使用
			Subject currentUser = SecurityUtils.getSubject();
	        if (!Util.isEmpty(currentUser)) {
	            Session session = currentUser.getSession();
	            if (null != session) {
	            	SessionInfo sessionInfo = new SessionInfo();
	    			sessionInfo.setUserId(user.getCid());
	    			sessionInfo.setLoginName(user.getCname());
	    			
	     			User r = userService.findUserByLoginCid(user.getCid());
	    			sessionInfo.setRealName(r.getCrealname());
	    			sessionInfo.setUserGroup(r.getType());
	    			session.setAttribute(Const.SESSION_USER, user);
	                session.setAttribute(Const.SESSION_INFO, sessionInfo);

	            }
	        }
			return authenticationInfo;
		} else {
			return null;
		}
	}
	/*
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("-------授权-------");
		// 根据身份信息获取权限信息
		Login user = (Login) principals.getPrimaryPrincipal();
		
////		// 从数据库获取到权限数据
////		List<String> resourceIds =  user.getResourceIds();
////		
////		if(!Util.isEmpty(resourceIds)) {
////			List<String> resourceKeys =  new ArrayList<String>();
////			for (String resourceId : resourceIds) {
////				Resource resource = resourceService.findByCid(resourceId);
////				if(!Util.isEmpty(resource)) {
////					resourceKeys.add(resource.getCkey());
////				}
////			}
////			// 将上边查询到授权信息填充到simpleAuthorizationInfo对象中
////			SimpleAuthorizationInfo  simpleAuthorizationInfo = new SimpleAuthorizationInfo();
////			//角色
////			simpleAuthorizationInfo.addRole(user.getRole().getCkey());
//			//权限
//			simpleAuthorizationInfo.addStringPermissions(resourceKeys);
			
		// 将上边查询到授权信息填充到simpleAuthorizationInfo对象中
		SimpleAuthorizationInfo  simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//角色
		User r = userService.findUserByLoginCid(user.getCid());
		simpleAuthorizationInfo.addRole(r.getType());
		
		return simpleAuthorizationInfo;
	
	}
}
