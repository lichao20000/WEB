
package com.linkage.litms.common.tld.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.linkage.litms.common.tld.components.SuperAuth;
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
 * @see SuperAuth
 * @category com.linkage.litms.common.tld.ui
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SuperAuthTag extends ComponentTagSupport
{

	private static final long serialVersionUID = 7154983197438209481L;
	private String authCode;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res)
	{
		return new SuperAuth(stack);
	}

	protected void populateParams()
	{
		((SuperAuth) getComponent()).setAuthCode(authCode);
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
}
