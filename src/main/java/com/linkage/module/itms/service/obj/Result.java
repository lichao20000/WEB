
package com.linkage.module.itms.service.obj;

import java.io.Serializable;

/**
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2021年1月27日
 * @category com.linkage.module.itms.service.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class Result implements Serializable
{

	private static final long serialVersionUID = -8456697940128390977L;
	private int resultCode = 1002;
	private String resultDesc = "参数无效";

	public int getResultCode()
	{
		return resultCode;
	}

	public void setResultCode(int resultCode)
	{
		this.resultCode = resultCode;
	}

	public String getResultDesc()
	{
		return resultDesc;
	}

	public void setResultDesc(String resultDesc)
	{
		this.resultDesc = resultDesc;
	}

	@Override
	public String toString()
	{
		return "Result [resultCode=" + resultCode + ", resultDesc=" + resultDesc + "]";
	}
}
