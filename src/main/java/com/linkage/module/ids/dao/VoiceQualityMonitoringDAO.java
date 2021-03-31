
package com.linkage.module.ids.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年7月26日
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceQualityMonitoringDAO extends SuperDAO
{

	public int addVoiceQualityResult(String oui, String device_serialnumber,
			Map<String, String> map)
	{
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_voice_quality_result (oui, device_serialnumber,stattime, txpackets, rxpackets, meandelay, meanjitter,fractionloss, localipaddress, localudpport,farendipaddress,farendudpport,codec,moslq) values(?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");
		psql.setString(1, oui);
		psql.setString(2, device_serialnumber);
		psql.setString(3, StringUtil.getStringValue(map, "StatTime", ""));
		psql.setString(4, StringUtil.getStringValue(map, "TxPackets", ""));
		psql.setString(5, StringUtil.getStringValue(map, "RxPackets", ""));
		psql.setString(6, StringUtil.getStringValue(map, "MeanDelay", ""));
		psql.setString(7, StringUtil.getStringValue(map, "MeanJitter", ""));
		psql.setString(8, StringUtil.getStringValue(map, "FractionLoss", "")+"%");
		psql.setString(9, StringUtil.getStringValue(map, "LocalIPAddress", ""));
		psql.setString(10, StringUtil.getStringValue(map, "LocalUDPPort", ""));
		psql.setString(11, StringUtil.getStringValue(map, "FarEndIPAddress", ""));
		psql.setString(12, StringUtil.getStringValue(map, "FarEndUDPPort", ""));
		psql.setString(13, StringUtil.getStringValue(map, "Codec", ""));
		psql.setString(14, StringUtil.getStringValue(map, "MosLq", ""));
		int num = jt.update(psql.getSQL());
		return num;
	}
}
