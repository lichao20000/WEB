/**
 * 
 */

package com.linkage.module.gtms.stb.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class GetParaModeValueBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GetParaModeValueBIO.class);

	/**
	 * 查询终端节点值
	 * 
	 * @param deviceId
	 * @param paraV
	 * @return
	 */
	public List<Map<String, String>> getParaModelValue(String deviceId, String paraV)
	{
		logger.debug("getParaModelValue({},{})", deviceId, paraV);
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_STB);
		List<ParameValueOBJ> list = acsCorba.getValue(deviceId, paraV);
		if (null == list || list.isEmpty())
		{
			return null;
		}
		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();
		for (ParameValueOBJ obj : list)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", obj.getName());
			map.put("value", obj.getValue());
			map.put("type", obj.getType());
			valueList.add(map);
		}
		return valueList;
	}
}
