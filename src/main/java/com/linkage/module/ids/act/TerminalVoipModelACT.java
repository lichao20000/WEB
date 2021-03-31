
package com.linkage.module.ids.act;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.ids.bio.TerminalVoipModelBIO;

/**
 * 终端语音业务透传模式hb_lt
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-29
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class TerminalVoipModelACT implements SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(TerminalVoipModelACT.class);
	private TerminalVoipModelBIO bio;
	private String textDeviceId;
	private String selectMode;
	private String resultDesc;

	public String changeModel()
	{
		logger.warn("textDeviceId : " + textDeviceId + " " + "selectMode : " + selectMode);
		if (null == textDeviceId || textDeviceId.isEmpty())
		{
			resultDesc = "没有该设备信息";
			return "changeModelResult";
		}
		Map<String, String> devInfo = bio.queryDevInfoByDeviceId(textDeviceId);
		String vendorId = StringUtil.getStringValue(devInfo, "vendor_id");
		String deviceId = StringUtil.getStringValue(devInfo, "device_id");
		logger.warn("vendorId : " + vendorId + " " + "deviceId : " + deviceId);
		if (null == vendorId || vendorId.isEmpty())
		{
			resultDesc = "该设备没有厂商信息";
		}
		else
		{
			if ("2".equals(vendorId))
			{
				resultDesc = changeParam("1", selectMode, deviceId);
			}
			else
			{
				resultDesc = changeParam("0", selectMode, deviceId);
			}
		}
		logger.warn("resultDesc : " + resultDesc);
		return "changeModelResult";
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
	}

	/**
	 * 下发设备设置节点值 节点参数类型 1 string,2 int,3 unsignedInt,4 boolean 厂商类型: 1 华为 0 非华为 模式类型: 1
	 * Fax 0 Modem
	 * 
	 * @param vendorType
	 * @param changeModel
	 * @return
	 */
	private String changeParam(String vendorType, String changeModel, String deviceId)
	{
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_ITMS);
		ArrayList<ParameValueOBJ> paramList = new ArrayList<ParameValueOBJ>();
		String returnMessage = "";
		if ("0".equals(vendorType))
		{
			if (!"1".equals(changeModel))
			{
				returnMessage = "非华为猫只有传真模式!";
				return returnMessage;
			}
			else
			{
				ParameValueOBJ parameValueOBJ = new ParameValueOBJ();
				parameValueOBJ
						.setName("InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.FaxT38.Enable");
				parameValueOBJ.setValue("1");
				parameValueOBJ.setType("4");
				paramList.add(parameValueOBJ);
			}
		}
		else
		{
			ParameValueOBJ parameValueOBJEnable = new ParameValueOBJ();
			parameValueOBJEnable
					.setName("InternetGatewayDevice.Services.VoiceService.1.PhyInterface.1.X_HW_DspTemplate.Enable");
			parameValueOBJEnable.setValue("1");
			parameValueOBJEnable.setType("4");
			ParameValueOBJ parameValueOBJJbMode = new ParameValueOBJ();
			parameValueOBJJbMode
					.setName("InternetGatewayDevice.Services.VoiceService.1.PhyInterface.1.X_HW_DspTemplate.JbMode");
			parameValueOBJJbMode.setValue("Static");
			parameValueOBJJbMode.setType("1");
			ParameValueOBJ parameValueOBJNLP = new ParameValueOBJ();
			parameValueOBJNLP
					.setName("InternetGatewayDevice.Services.VoiceService.1.PhyInterface.1.X_HW_DspTemplate.NLP");
			parameValueOBJNLP.setValue("Closed");
			parameValueOBJNLP.setType("1");
			ParameValueOBJ parameValueOBJWorkMode = new ParameValueOBJ();
			if ("1".equals(changeModel))
			{
				parameValueOBJWorkMode
						.setName("InternetGatewayDevice.Services.VoiceService.1.PhyInterface.1.X_HW_DspTemplate.WorkMode");
				parameValueOBJWorkMode.setValue("Fax");
				parameValueOBJWorkMode.setType("1");
			}
			else
			{
				parameValueOBJWorkMode
						.setName("InternetGatewayDevice.Services.VoiceService.1.PhyInterface.1.X_HW_DspTemplate.WorkMode");
				parameValueOBJWorkMode.setValue("Modem");
				parameValueOBJWorkMode.setType("1");
			}
			paramList.add(parameValueOBJEnable);
			paramList.add(parameValueOBJJbMode);
			paramList.add(parameValueOBJNLP);
			paramList.add(parameValueOBJWorkMode);
		}
		int statusResult = acsCorba.testConnection(deviceId, Global.GW_TYPE_ITMS);
		logger.warn("statusResult : " + statusResult);
		if ((1 != statusResult) && (-3 != statusResult))
		{
			returnMessage = "设备不在线";
			return returnMessage;
		}
		int flag = acsCorba.setValue(deviceId, paramList);
		returnMessage = resultMessage(flag);
		return returnMessage;
	}

	private String resultMessage(int flag)
	{
		String returnMessage = "";
		if (0 == flag || 1 == flag)
		{
			returnMessage = "模式修改成功！";
		}
		else if (-7 == flag)
		{
			returnMessage = "系统参数错误";
		}
		else if (-6 == flag)
		{
			returnMessage = "设备正被操作";
		}
		else if (-1 == flag)
		{
			returnMessage = "设备连接失败";
		}
		else if (-9 == flag)
		{
			returnMessage = "系统内部错误";
		}
		else
		{
			returnMessage = "TR069错误";
		}
		return returnMessage;
	}

	public String getTextDeviceId()
	{
		return textDeviceId;
	}

	public void setTextDeviceId(String textDeviceId)
	{
		this.textDeviceId = textDeviceId;
	}

	public String getSelectMode()
	{
		return selectMode;
	}

	public void setSelectMode(String selectMode)
	{
		this.selectMode = selectMode;
	}

	public TerminalVoipModelBIO getBio()
	{
		return bio;
	}

	public void setBio(TerminalVoipModelBIO bio)
	{
		this.bio = bio;
	}

	public String getResultDesc()
	{
		return resultDesc;
	}

	public void setResultDesc(String resultDesc)
	{
		this.resultDesc = resultDesc;
	}
}
