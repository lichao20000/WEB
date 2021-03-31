
package com.linkage.module.gtms.stb.config.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.config.dao.StreamToolDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamToolBIO
{

	private static Logger logger = LoggerFactory.getLogger(StreamToolBIO.class);
	private StreamToolDAO streamToolDAO;

	public List getAcsStream(String deviceId)
	{
		logger.debug("getAcsStream({})", deviceId);
		List retList = null;
		List streamList = this.streamToolDAO.queryDeviceStream(deviceId);
		if ((streamList != null) && (!(streamList.isEmpty())))
		{
			logger.debug("getAcsStream, has data");
			retList = new ArrayList();
			int size = streamList.size();
			int red = 0;
			String tmp = "";
			for (int i = 0; i < size; ++i)
			{
				Map rMap = (Map) streamList.get(i);
				Map retMap = new HashMap();
				if (StringUtil.IsEmpty(tmp))
				{
					tmp = StringUtil.getStringValue(rMap.get("s_ip"));
				}
				if (tmp.equals(rMap.get("s_ip")))
				{
					red = 1;
				}
				else
				{
					red = 2;
				}
				retMap.put("source",
						"From:" + rMap.get("s_ip") + ":" + rMap.get("s_port"));
				retMap.put("dest", "To:" + rMap.get("d_ip") + ":" + rMap.get("d_port"));
				retMap.put("time",
						new DateTimeUtil(StringUtil.getLongValue(rMap.get("inter_time")))
								.getLongDate());
				retMap.put("content", rMap.get("inter_content"));
				retMap.put("red", Integer.valueOf(red));
				retList.add(retMap);
			}
		}
		else
		{
			logger.warn("getAcsStream, no data ");
		}
		return retList;
	}

	public int clearAcsStream(String deviceId)
	{
		return this.streamToolDAO.removeDeviceStream(deviceId);
	}

	public StreamToolDAO getStreamToolDAO()
	{
		return this.streamToolDAO;
	}

	public void setStreamToolDAO(StreamToolDAO streamToolDAO)
	{
		this.streamToolDAO = streamToolDAO;
	}
}