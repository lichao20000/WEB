package bio.report;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.report.NetWarnQueryDAO;

/**
 * 返回告警等级及其对应的告警数量
 * 
 * @author suixz(5253) tel(13512508857)
 * @since 2008-01-16
 * @version 1.0
 * @category report
 */
public class NetWarnQueryBIO
{
	private NetWarnQueryDAO netWarnQueryDAO;
	/**
	 * 返回告警等级及其对应的告警数量
	 * 
	 * @param severity
	 *            告警等级
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param deviceIp
	 *            设备IP
	 * @param deviceName
	 *            设备名称
	 * @param actstatus
	 *            确认状态
	 * @param clearstatus
	 * 			  清除状态
	 * @param creatortype
	 *            告警类型
	 * @param city_id:
	 * 			  属地
	 * @param areaId
	 *            域id
	 * @param isAdmin
	 *            amdin域标志 true:是,false:否
	 * @return
	 * @throws ParseException
	 */
	public List<Map> getSeverityAndWarnNum(String resourceType, String gatherId,
			String severity, long startTime, long endTime, String deviceIp,
			String deviceName,String actstatus,String clearstatus,String creatortype,String city_id, long areaId, boolean isAdmin) throws ParseException,
			SQLException
	{
		List<Map> list = netWarnQueryDAO.getSeverityAndWarnNum(resourceType, gatherId,
				severity, startTime, endTime, deviceIp, deviceName,actstatus,clearstatus,creatortype,city_id, areaId, isAdmin);
		List<Map> tmp = new ArrayList<Map>();
		Map<String, String> map = new HashMap();
		if (list == null)
			{
				return null;
			}
		for (Map m : list)
			{
				if (severity.contains(m.get("severity").toString()))
					{
						if (map.get(m.get("severity").toString()) == null)
							{
								map.put(m.get("severity").toString(), m.get("num")
										.toString());
							}
						else
							{
								map.put(m.get("severity").toString(),
										String
												.valueOf((Integer.parseInt(map.get(
														m.get("severity").toString())
														.toString()) + Integer.parseInt(m
														.get("num").toString()))));
							}
					}
			}
		for (Map.Entry<String, String> entry : map.entrySet())
			{
				Map m = new HashMap();
				m.put("severity", entry.getKey());
				m.put("num", entry.getValue());
				tmp.add(m);
			}
		return tmp;
	}
	public void setNetWarnQueryDAO(NetWarnQueryDAO netWarnQueryDAO)
	{
		this.netWarnQueryDAO = netWarnQueryDAO;
	}
}
