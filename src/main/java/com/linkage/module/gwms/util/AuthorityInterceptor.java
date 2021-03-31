/**
 * 
 */
package com.linkage.module.gwms.util;

import java.util.Map;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-12-24
 * @category com.linkage.module.commons.interceptor
 * 
 */
public class AuthorityInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1358600090729208361L;
	//拦截Action处理的拦截方法
	public String intercept(ActionInvocation invocation) throws Exception {
		// 取得请求相关的ActionContext实例
		ActionContext ctx = invocation.getInvocationContext();
		Map session=ctx.getSession();
		//取出名为user的session属性
		UserRes user = (UserRes)session.get("curUser");
		//如果没有登陆，或者登陆所有的用户名不是aumy，都返回重新登陆
		if(null!=user){
			return invocation.invoke();
		}
		return "login";
	}
}
