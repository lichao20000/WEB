
package com.linkage.litms.common.tld.components;

import java.io.Writer;
import java.util.Set;

import org.apache.struts2.components.Component;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <pre>
 * 超级权限标签组件。
 * 该组件是基于struts2标签开发
 * 目前只按一个权限简码(authCode)判断当前用户是否拥有该超级权限，
 * 并且受系统配置文件liposs_cfg.xml中enableSuperAuth配置限制。如果不启用超级权限，则该标签始终返回true
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-14
 * @category com.linkage.litms.common.tld.components
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SuperAuth extends Component
{

	private String authCode;

	public SuperAuth(ValueStack stack)
	{
		super(stack);
	}

	public boolean start(Writer writer)
	{
		// 1. 如果系统配置不启用超级权限，always return true
		if (!LipossGlobals.enableSuperAuth())
		{
			return true;
		}
		//2.获取当前登录用户的超级权限集合
		Set<String> userSuperAuths = WebUtil.getCurrentUserSuperAuths();
		return userSuperAuths.contains(authCode);
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
}
