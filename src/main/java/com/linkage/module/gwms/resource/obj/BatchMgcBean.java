
package com.linkage.module.gwms.resource.obj;

import java.io.Serializable;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-9-26
 * @category com.linkage.module.gwms.resource.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchMgcBean implements Serializable
{

	/**
	 * MGC 导入Excel文件的标题头，用于Excel文件合法性校验
	 */
	public static final String[] MGC_TITLES = new String[] { "逻辑ID*", "主MGC地址*",
			"主MGC端口*", "备MGC地址*", "备MGC端口*", "电话号码*" };
	/**
	 * MGC Excel导入将cell内容映射到java bean的属性名
	 */
	public static final String[] MGC_BEAN_PROPERTIES = new String[] { "username",
			"mainMgcIp", "mainMgcPort", "standMgcIp", "standMgcPort", "voipPhone" };
	private static final long serialVersionUID = 8923853439356905889L;
	/**
	 * 工单受理时间
	 */
	// private String dealdate;
	/**
	 * 逻辑SN
	 */
	private String username;
	/**
	 * 接入方式 (订单类型)
	 */
	// private String accessType;
	/**
	 * 接入方式对应的表主键ID
	 */
	// private String accessTypeId;
	/**
	 * 联系人用户姓名
	 */
	// private String linkman;
	// 家庭住址
	// private String homeAddr;
	// 属地
	// private String cityId;
	// 属地
	// private String cityName;
	/**
	 * 宽带账号
	 */
	// private String netUsername;
	/**
	 * 宽带密码
	 */
	// private String netPassword;
	/**
	 * 宽带vlanID
	 */
	// private String netVlanID;
	/**
	 * 主MGC地址
	 */
	private String mainMgcIp;
	/**
	 * 主MGC端口
	 */
	private String mainMgcPort;
	/**
	 * 备MGC地址
	 */
	private String standMgcIp;
	/**
	 * 备MGC端口
	 */
	private String standMgcPort;
	/**
	 * 语音注册域名（软交换）
	 */
	// private String regDomain;
	/**
	 * 终端物理标示（TID与语音端口间存在对应关系）
	 */
	// private String voipPort;
	/**
	 * 电话号码,作为voip电话，必填项
	 */
	private String voipPhone;
	/**
	 * iptv账号
	 */
	// private String iptvUsername;
	/**
	 * IPTV个数
	 */
	// private String iptvNum;
	/**
	 * 用户类型(家庭,企业)
	 */
	// private String userType;
	/**
	 * 用户类型(1=家庭,2=企业)
	 */
	// private String userTypeCode;
	/**
	 * 客户ID
	 */
	// private String customerId;
	/**
	 * 客户帐号
	 */
	// private String customerAccount;
	/**
	 * 客户密码
	 */
	// private String customerPwd;
	/**
	 * Excel导入失败行数
	 */
	private int failedLine;
	/**
	 * Excel导入失败原因
	 */
	private String failedCause;

	public int getFailedLine()
	{
		return failedLine;
	}

	public void setFailedLine(int failedLine)
	{
		this.failedLine = failedLine;
	}

	public String getFailedCause()
	{
		return failedCause;
	}

	public void setFailedCause(String failedCause)
	{
		this.failedCause = failedCause;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getMainMgcIp()
	{
		return mainMgcIp;
	}

	public void setMainMgcIp(String mainMgcIp)
	{
		this.mainMgcIp = mainMgcIp;
	}

	public String getMainMgcPort()
	{
		return mainMgcPort;
	}

	public void setMainMgcPort(String mainMgcPort)
	{
		this.mainMgcPort = mainMgcPort;
	}

	public String getStandMgcIp()
	{
		return standMgcIp;
	}

	public void setStandMgcIp(String standMgcIp)
	{
		this.standMgcIp = standMgcIp;
	}

	public String getStandMgcPort()
	{
		return standMgcPort;
	}

	public void setStandMgcPort(String standMgcPort)
	{
		this.standMgcPort = standMgcPort;
	}

	public String getVoipPhone()
	{
		return voipPhone;
	}

	public void setVoipPhone(String voipPhone)
	{
		this.voipPhone = voipPhone;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BatchMgcBean [username=");
		builder.append(username);
		builder.append(", mainMgcIp=");
		builder.append(mainMgcIp);
		builder.append(", mainMgcPort=");
		builder.append(mainMgcPort);
		builder.append(", standMgcIp=");
		builder.append(standMgcIp);
		builder.append(", standMgcPort=");
		builder.append(standMgcPort);
		builder.append(", voipPhone=");
		builder.append(voipPhone);
		builder.append(", failedLine=");
		builder.append(failedLine);
		builder.append(", failedCause=");
		builder.append(failedCause);
		builder.append("]");
		return builder.toString();
	}
}
