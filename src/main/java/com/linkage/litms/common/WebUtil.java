
package com.linkage.litms.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionContext;

/**
 * <pre>
 * Web相关的公用类
 * 提供WEB相关的公用类，比如从session中获取当前用户信息。
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-15
 * @category com.linkage.litms.common
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class WebUtil
{

	/**
	 * <pre>
	 * 用户保持在session中指定的key。
	 * 建议所有涉及用户session key 都使用该常量
	 * </pre>
	 */
	public static final String CURRENT_USER_SESSION_KEY = "curUser";
	/**
	 * 当前用户的超级权限集合保持在session中key
	 */
	public static final String SUPER_AUTHS_SESSION_KEY = "ailk-itms-web_superAuthKey";
	
	/**
	 * <pre>
	 * 获取当前的登录用户
	 * 从session中获取当前用户信息，该方法做简单封装。
	 * 当用户没有登录的情况下，该方法返回null
	 * </pre>
	 * 
	 * @return 返回当前登录用户信息，但用户没有登录的情况下，该方法返回null
	 */
	public static final UserRes getCurrentUser()
	{
		return (UserRes) ActionContext.getContext().getSession()
				.get(CURRENT_USER_SESSION_KEY);
	}

	/**
	 * <pre>
	 * 获取当前登录用户的超级权限集合
	 * 1.根据当前用户的账号查询超级权限集合
	 * 2.根据当前用户的角色账号查询超级权限集合
	 * 3.如果当前用户没有任何集合，则返回空集合，表示没有任何超级权限。
	 * 4.用于用户账号和角色可能绑定了同一个超级权限，所以使用set，不重复保存。
	 * 5.当前用户的超级权限集合是从数据查询，故返回的超级权限集合是不用修改的。
	 * </pre>
	 * 
	 * @return 返回当前用户的超级权限集合，用户没有登录的情况下，返回空集合。never null。
	 */
	public static final Set<String> getCurrentUserSuperAuths()
	{
		// 1.从session中获取超级权限
		@SuppressWarnings("unchecked")
		Set<String> superAuths = (Set<String>) ActionContext.getContext().getSession()
				.get(SUPER_AUTHS_SESSION_KEY);
		if (superAuths != null)
		{
			return superAuths;
		}
		UserRes curUser = getCurrentUser();
		if (curUser == null)
		{
			// 2.当前用户没有登录，不加载超级权限
			superAuths = new HashSet<String>(0);
		}
		else
		{
			// 3.根据当前用户ID和角色ID查询当前用户的超级权限集合
			superAuths = new HashSet<String>();
			initAuthsByAccount(superAuths);
			initAuthsByRole(superAuths);
		}
		// 4.将超级权限集合置为不可更改
		Set<String> unmodifiableSuperAuths = Collections.unmodifiableSet(superAuths);
		ActionContext.getContext().getSession().put(SUPER_AUTHS_SESSION_KEY, unmodifiableSuperAuths);
		return unmodifiableSuperAuths;
	}

	private static void initAuthsByAccount(Set<String> existSuperAuths)
	{
		PrepareSQL pSql = new PrepareSQL(
				"select t1.auth_code from t_sys_auth t1, t_sys_authrelation t2"
						+ " where t1.auth_id = t2.auth_id and t2.relation_type = 0 and t2.relation_id = ?");
		long userId = getCurrentUser().getUser().getId();
		int index = 0;
		pSql.setLong(++index, userId);
		Set<String> result = DBOperation.getStrRecords(pSql.getSQL());
		if (result != null)
		{
			existSuperAuths.addAll(result);
		}
	}

	private static void initAuthsByRole(Set<String> existSuperAuths)
	{
		PrepareSQL pSql = new PrepareSQL(
				"select t1.auth_code from t_sys_auth t1, t_sys_authrelation t2"
						+ " where t1.auth_id = t2.auth_id and t2.relation_type = 1 and t2.relation_id = ?");
		long userRoleId = getCurrentUser().getUser().getRoleId();
		int index = 0;
		pSql.setLong(++index, userRoleId);
		Set<String> result = DBOperation.getStrRecords(pSql.getSQL());
		if (result != null)
		{
			existSuperAuths.addAll(result);
		}
	}
	
	public static final String getCurrentUserIP() {
		return ServletActionContext.getRequest().getRemoteAddr();
	}
}
