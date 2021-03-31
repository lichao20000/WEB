/**
 * 
 */

package com.linkage.module.gtms.stb.config.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.config.bio.GetParaModeValueBIO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class GetParaModeValueACT
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GetParaModeValueACT.class);
	private String paraV = null;
	private String deviceId = null;
	private GetParaModeValueBIO paraModeVlaueBio = null;
	private ParameValueOBJ parameValue = null;
	private List<Map<String, String>> valueList = null;

	/**
	 * execute
	 */
	public String execute() throws Exception
	{
		logger.debug("execute()");
		return "success";
	}

	/**
	 * execute
	 */
	public String getOneValue() throws Exception
	{
		logger.debug("execute()");
		this.valueList = paraModeVlaueBio.getParaModelValue(deviceId, paraV);
		return "oneValue";
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the parameValue
	 */
	public ParameValueOBJ getParameValue()
	{
		return parameValue;
	}

	/**
	 * @param parameValue
	 *            the parameValue to set
	 */
	public void setParameValue(ParameValueOBJ parameValue)
	{
		this.parameValue = parameValue;
	}

	/**
	 * @return the paraModeVlaueBio
	 */
	public GetParaModeValueBIO getParaModeVlaueBio()
	{
		return paraModeVlaueBio;
	}

	/**
	 * @param paraModeVlaueBio
	 *            the paraModeVlaueBio to set
	 */
	public void setParaModeVlaueBio(GetParaModeValueBIO paraModeVlaueBio)
	{
		this.paraModeVlaueBio = paraModeVlaueBio;
	}

	/**
	 * @return the paraV
	 */
	public String getParaV()
	{
		return paraV;
	}

	/**
	 * @param paraV
	 *            the paraV to set
	 */
	public void setParaV(String paraV)
	{
		this.paraV = paraV;
	}

	/**
	 * @return the valueList
	 */
	public List<Map<String, String>> getValueList()
	{
		return valueList;
	}

	/**
	 * @param valueList
	 *            the valueList to set
	 */
	public void setValueList(List<Map<String, String>> valueList)
	{
		this.valueList = valueList;
	}
}
