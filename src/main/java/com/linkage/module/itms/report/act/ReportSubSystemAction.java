
package com.linkage.module.itms.report.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.LoginUtil;
import com.linkage.litms.system.User;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <pre>
 * ITMS WEB报表系统拆分
 * 将ITMS WEB报表系统拆分，分两个WEB服务部署，分别为master和slaver。
 * master和slaver使用同一套web系统代码。
 * 当master点击WEB报表系统连接，调用该Action，使用重定向至slaver登录操作，并且将用户的信息加密后传递。
 * slaver接受到用户信息，将用户信息解密，直接调用原来的用户名密码登录逻辑，完成二次登录后，直接跳转到slaver系统。
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2014-3-24
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ReportSubSystemAction extends ActionSupport
{

	private static final long serialVersionUID = -6868745104366443714L;
	private static Logger logger = LoggerFactory
			.getLogger(ReportSubSystemAction.class);
	private static String url = LipossGlobals.getLipossProperty("ClusterMode.url");
	private String param = "";

	public String execute()
	{
		User user = WebUtil.getCurrentUser().getUser();
		String areaName = StringUtil.getStringValue(ResTool.getAnyArea(user.getAreaId()),
				"area_name");
		param = LoginUtil.encodeUser(user.getAccount(), user.getPasswd(), areaName);
		logger.warn("url=[{}?requestParam={}]", url, param);
		return "redirect";
	}

	public String getUrl()
	{
		return url;
	}

	public String getParam()
	{
		return param;
	}

	public void setParam(String param)
	{
		this.param = param;
	}
}
