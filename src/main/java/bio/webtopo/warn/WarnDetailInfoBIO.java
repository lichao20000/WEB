package bio.webtopo.warn;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.webtopo.warn.filter.ConstantEventEnv;

import com.linkage.litms.common.util.DateTimeUtil;

import dao.webtopo.warn.WarnDetailInfoDAO;

/**
 * WebTopo实时告警牌告警详情BIO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-19
 * @category WebTopo/实时告警牌/告警详情
 * 
 */
public class WarnDetailInfoBIO
{
	private WarnDetailInfoDAO warnDetailInfoDao = null;

	private static final Logger LOG = LoggerFactory.getLogger(WarnDetailInfoBIO.class);

	/**
	 * 告警序列号
	 */
	private String serialNo = null;

	/**
	 * 子序列号
	 */
	private int subSerialNo = 0;

	/**
	 * 采集点
	 */
	private String gatherId = null;

	/**
	 * 告警创建时间(秒)
	 */
	private long createTime = 0;

	public void setWarnDetailInfoDao(WarnDetailInfoDAO warnDetailInfoDao)
	{
		this.warnDetailInfoDao = warnDetailInfoDao;
	}

	/**
	 * 通过告警序列号,子序列号,采集点查询告警详情,包含SysLog日志的相关信息
	 * 
	 * @param serialNo
	 *            告警序列号
	 * @param subSerialNo
	 *            子序列号,由WarnDetailInfoAction写死,都是0
	 * @param gatherId
	 *            采集点
	 * @param createTime
	 *            创建时间(秒),用来拼表名(例如:event_raw_2008_16)
	 * @return
	 */
	public List<Map>[] getWarnDetailInfo(String serialNo, int subSerialNo,
			String gatherId, long createTime)
	{
		this.serialNo = serialNo;
		this.subSerialNo = subSerialNo;
		this.gatherId = gatherId;
		this.createTime = createTime;
		LOG.debug("createTime = " + this.createTime);
		if (this.createTime == 0)
		{
			return new ArrayList[2];
		}
		// 获取告警所在的表名
		String tableName = getTableNameByCreateTime(this.createTime);
		LOG.debug("tableName = " + tableName);
		// 查询数据
		List list = warnDetailInfoDao.getWarnDetailInfo(this.serialNo,
				this.subSerialNo, this.gatherId, tableName);
		// 获取告警所在设备ID
		String deviceId = getDeviceIdFromList(list);
		// 修正一些查询结果
		dealResultList(list);
		// 通过设备ID获取该设备的SysLog日志内容
		List sysLogList = warnDetailInfoDao.getSysLogInfo(deviceId);
		// 将两个List构成一个数组
		List[] listArr = new ArrayList[2];
		listArr[0] = list;
		listArr[1] = sysLogList;
		// listArr[1] = getMyTestSysLogList();
		return listArr;
	}

	/**
	 * 添加告警知识库内容
	 * 
	 * @param firstTime
	 *            是否是添加 1:添加 0:修改
	 * @param operation
	 *            0:确认 1:清除
	 * @param serialNo
	 *            序列号
	 * @param subSerialNo
	 *            子序列号(WarnDetailInfoAction写死0)
	 * @param gatherId
	 *            采集点
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param acc_loginname
	 *            确认记录创建人,即当前登陆用户
	 * @param createTime
	 *            告警创建时间
	 * @param warnReason
	 *            故障原因
	 * @param warnResove
	 *            解决方法
	 * @param curTime
	 *            告警确认时间,即当前时间
	 * @param deviceType
	 *            告警设备类型
	 * @param sourceIP
	 *            告警源设备IP
	 * @param createType
	 *            告警创建者类型
	 * @param sourceName
	 *            告警源设备名称
	 * @return
	 */
	public boolean saveTabKnowledgeInfo(int firstTime, String serialNo,
			int subSerialNo, String gatherId, String remark, String subject,
			String content, String acc_loginname, long createTime,
			String warnReason, String warnResove, long curTime, int deviceType,
			String sourceIP, int createType, String sourceName)
	{
		// 修正3个字段
		remark = (remark == null ? "" : remark.trim());
		warnReason = (warnReason == null ? "" : warnReason.trim());
		warnResove = (warnResove == null ? "" : warnResove.trim());

		boolean flag = false;
		boolean flag1 = false;
		LOG.debug("createTime = " + createTime);
		if (createTime == 0)
		{
			LOG.debug("传入告警创建时间为 0 ,告警确认或者清除失败");
			return flag;
		}
		// 获取告警所在的表名
		String tableName = getTableNameByCreateTime(createTime);
		LOG.debug("tableName = " + tableName);

		flag1 = warnDetailInfoDao.saveRemarkInfo(serialNo, subSerialNo,
				gatherId, tableName, remark);
		LOG.debug("保存告警知识库执行结果 flag = " + flag);
		if (firstTime == 1)
		{
			flag = warnDetailInfoDao.addTabKnowledgeInfo(serialNo, subSerialNo,
					gatherId, subject, content, acc_loginname, createTime,
					warnReason, warnResove, curTime, deviceType, sourceIP,
					createType, sourceName);
		} else
			flag = warnDetailInfoDao.updateTabKnowledgeInfo(serialNo,
					subSerialNo, gatherId, warnReason, warnResove);
		LOG.debug("保存告警知识库执行结果 firstTime = " + firstTime + " & flag = "
				+ flag);
		return flag && flag1;
	}

	/**
	 * 通过告警创建时间获取该告警所在的表名
	 * 
	 * @param createTime
	 *            创建时间(秒)
	 * @return
	 */
	private String getTableNameByCreateTime(long createTime)
	{
		DateTimeUtil dateUtil = new DateTimeUtil(createTime * 1000);
		int year = dateUtil.getYear();
		int week = dateUtil.getWeekOfYear();
		return "event_raw_" + year + "_" + week;
	}

	/**
	 * 通过告警创建时间获取该告警所在的表的下一个表名
	 * 
	 * @param createTime
	 *            创建时间(秒)
	 * @return
	 */
	private String getNextTableNameByCreateTime(long createTime)
	{
		// 把传入时间加上一星期
		createTime = createTime + 7 * 24 * 60 * 60;
		DateTimeUtil dateUtil = new DateTimeUtil(createTime * 1000);
		int year = dateUtil.getYear();
		int week = dateUtil.getWeekOfYear();
		return "event_raw_" + year + "_" + week;
	}

	/**
	 * 从查询出来的列表中获取设备ID
	 * 
	 * @param list
	 * @return
	 */
	private String getDeviceIdFromList(List list)
	{
		if (list == null || list.size() == 0)
			return null;
		Map map = (Map) list.get(0);
		if (map == null)
			return null;
		// 获取设备ID
		String deviceId = (String) map.get("devicecoding");
		if (deviceId != null
				&& (deviceId.equals("NULL") || deviceId.equals("null")))
		{
			deviceId = null;
		}
		return deviceId;
	}

	/**
	 * 处理获得的结果集,将告警等级,创建时间,创建者类型, 确认状态,清除状态 修正成描述信息<br>
	 * 修正故障原因,解决方法,告警附加说明<br>
	 * 若排重次数大于0时需要查询出最后一次告警排重时间
	 * 
	 * @param list
	 *            从数据库查询获得的结果集
	 */
	private void dealResultList(List list)
	{
		String tempStr = null;
		BigDecimal tempTime = null;
		if (list == null || list.size() == 0)
			return;
		Map map = (Map) list.get(0);
		if (map == null)
			return;

		// 修正 创建时间
		long createTime = ((BigDecimal) map.get("createtime")).longValue();
		DateTimeUtil dateUtil = new DateTimeUtil(createTime * 1000);
		tempStr = dateUtil.getLongDate();
		LOG.debug("createTime = " + createTime + " % " + tempStr);
		map.put("createtime", tempStr);

		// 修正 确认时间
		tempTime = (BigDecimal) map.get("acktime");
		long acktime = 0;
		tempStr = "";
		if (tempTime != null)
		{
			acktime = tempTime.longValue();
			if (acktime > 0)
			{
				dateUtil = new DateTimeUtil(acktime * 1000);
				tempStr = dateUtil.getLongDate();
			}
		}
		LOG.debug("acktime = " + acktime + " % " + tempStr);
		map.put("acktime", tempStr);

		// 修正 清除时间
		tempTime = (BigDecimal) map.get("cleartime");
		long cleartime = 0;
		tempStr = "";
		if (tempTime != null)
		{
			cleartime = tempTime.longValue();
			if (cleartime > 0)
			{
				dateUtil = new DateTimeUtil(cleartime * 1000);
				tempStr = dateUtil.getLongDate();
			}
		}
		LOG.debug("cleartime = " + cleartime + " % " + tempStr);
		map.put("cleartime", tempStr);

		// 修正 告警历时时间
		tempStr = getSpendTimeStr(createTime, acktime, cleartime);
		LOG.debug("spendtime = " + tempStr);
		map.put("spendtimestr", tempStr);

		// 修正 创建者类型
		int creatorType = ((BigDecimal) map.get("creatortype")).intValue();
		tempStr = getCreatorType(creatorType);
		LOG.debug("creatorType = " + creatorType + " % " + tempStr);
		map.put("creatortype", tempStr);
		map.put("creatortypeoriginal", creatorType);

		// 修正 告警等级
		int severity = ((BigDecimal) map.get("severity")).intValue();
		tempStr = ConstantEventEnv.NUM_LEVEL_MAP.get(severity);
		LOG.debug("severity = " + severity + " % " + tempStr);
		map.put("severity", tempStr);

		// 修正 设备型号
		String deviceModel = (String) map.get("device_model");
		if (deviceModel == null || deviceModel.equals("NULL")
				|| deviceModel.equals("null"))
		{
			tempStr = "";
		} else
		{
			tempStr = deviceModel;
		}
		LOG.debug("deviceModel = " + deviceModel + " % " + tempStr);
		map.put("device_model", tempStr);

		// 修正 设备类型(device_type)
		String deviceType = (String) map.get("device_type");
		if (deviceType == null || deviceType.equals("NULL")
				|| deviceType.equals("null"))
		{
			tempStr = "";
		} else
		{
			tempStr = deviceType;
		}
		LOG.debug("deviceType = " + deviceType + " % " + tempStr);
		map.put("device_type", tempStr);

		// 修正 设备详细地址(device_addr)
		String deviceAddr = (String) map.get("device_addr");
		if (deviceAddr == null || deviceAddr.equals("NULL")
				|| deviceAddr.equals("null"))
		{
			tempStr = "";
		} else
		{
			tempStr = deviceAddr;
		}
		LOG.debug("deviceAddr = " + deviceAddr + " % " + tempStr);
		map.put("device_addr", tempStr);

		// 修正 确认状态
		int activestatus = ((BigDecimal) map.get("activestatus")).intValue();
		tempStr = ConstantEventEnv.STATUS_NAME_MAP.get(activestatus);
		LOG.debug("activestatus = " + activestatus + " % " + tempStr);
		map.put("activestatus", tempStr);

		// 修正 清除状态
		int clearstatus = ((BigDecimal) map.get("clearstatus")).intValue();
		tempStr = ConstantEventEnv.STATUS_CLEAR_MAP.get(clearstatus);
		LOG.debug("clearstatus = " + clearstatus + " % " + tempStr);
		map.put("clearstatus", tempStr);

		// 修正 告警附加说明
		String remark = (String) map.get("remark");
		if (remark == null || remark.trim().equals("") || remark.equals("null")
				|| remark.equals("NULL"))
		{
			map.put("remark", "");
		}

		// 修正 故障原因
		String warnresove = (String) map.get("warnresove");
		if (warnresove == null || warnresove.trim().equals("")
				|| warnresove.equals("null") || warnresove.equals("NULL"))
		{
			map.put("warnresove", "");
		}

		// 修正 解决方法
		String warnreason = (String) map.get("warnreason");
		if (warnreason == null || warnreason.trim().equals("")
				|| warnreason.equals("null") || warnreason.equals("NULL"))
		{
			map.put("warnreason", "");
		}

		// 判断告警知识库表中是否有数据,没有的话页面提交时insert,否则update
		String firsttime = (String) map.get("firsttime");
		if (firsttime == null || firsttime.equals("")
				|| firsttime.equals("null") || firsttime.equals("NULL"))
		{
			LOG.debug("firsttime = 1");
			map.put("firsttime", "1");
		} else
		{
			LOG.debug("firsttime = 0");
			map.put("firsttime", "0");
		}

		// 排重次数,当排重次数大于0时需要查询最后一次告警排重时间
		int filttimes = ((BigDecimal) map.get("filttimes")).intValue();
		String lastFilterCreateTime = "";
		if (filttimes > 0)
		{
			tempStr = getLastFilterCreateTime(filttimes);
			if (tempStr != null)
				lastFilterCreateTime = tempStr;
		}
		map.put("lastfiltercreatetime", lastFilterCreateTime);
		LOG.debug("filttimes = " + filttimes + " % lastFilterCreateTime = "
				+ lastFilterCreateTime);
	}

	/**
	 * 获取告警历时时间的页面展示格式
	 * 
	 * @param createTime
	 *            告警创建时间(秒)
	 * @param ackTime
	 *            告警确认时间(秒)
	 * @param clearTime
	 *            告警清除时间(秒)
	 * @return 告警历时时间页面展示格式
	 */
	private String getSpendTimeStr(long createTime, long ackTime, long clearTime)
	{
		String str = "";
		long minLong = 60;
		long hourLong = 60 * 60;
		long dayLong = 60 * 60 * 24;
		long spendTime = getSpendTime(createTime, ackTime, clearTime);
		if (spendTime < minLong)
		{
			str = spendTime + " 秒";
		} else if (spendTime >= minLong && spendTime < hourLong)
		{
			long min = spendTime / minLong;
			long sec = spendTime % minLong;
			str = min + " 分 " + sec + " 秒";
		} else if (spendTime >= hourLong && spendTime < dayLong)
		{
			long hour = spendTime / hourLong;
			long minall = spendTime % hourLong;
			long min = minall / minLong;
			long sec = minall % minLong;
			str = hour + " 小时 " + min + " 分 " + sec + " 秒";
		} else if (spendTime >= dayLong)
		{
			long day = spendTime / dayLong;
			long hourall = spendTime % dayLong;
			long hour = hourall / hourLong;
			long minall = hourall % hourLong;
			long min = minall / minLong;
			long sec = minall % minLong;
			str = day + " 天 " + hour + " 小时 " + min + " 分 " + sec + " 秒";
		}
		return str;
	}

	/**
	 * 获取告警历时时间
	 * 
	 * @param createTime
	 *            告警创建时间(秒)
	 * @param ackTime
	 *            告警确认时间(秒)
	 * @param clearTime
	 *            告警清除时间(秒)
	 * @return 告警历时时间(秒)
	 */
	private long getSpendTime(long createTime, long ackTime, long clearTime)
	{
		long spendTime = 0;
		if (ackTime == 0 && clearTime == 0)
		{
			// 若此告警 未确认 未清除
			// 历时时间为创建时间到现在时间点
			spendTime = (System.currentTimeMillis() / 1000L) - createTime;
		} else if (ackTime != 0 && clearTime == 0)
		{
			// 若此告警 已确认,未清除
			// 历时时间为创建时间到确认时间
			spendTime = ackTime - createTime;
		} else if (ackTime == 0 && clearTime != 0)
		{
			// 若此告警 已清除,未确认
			// 历时时间为创建时间到清除时间
			spendTime = clearTime - createTime;
		} else if (ackTime <= clearTime)
		{
			// 若此告警 先确认,后清除
			// 历时时间为创建时间到确认时间
			spendTime = ackTime - createTime;
		} else
		{
			// 若此告警 先清除,后确认
			// 历时时间为创建时间到清除时间
			spendTime = clearTime - createTime;
		}
		return spendTime;
	}

	/**
	 * 获取最后一次告警排重时间
	 * 
	 * @param filttimes
	 *            排重次数
	 * @return 最后一次排重时间
	 */
	private String getLastFilterCreateTime(int filttimes)
	{
		// 当前告警所在表
		String tableNameA = getTableNameByCreateTime(createTime);
		// 当前告警所在表的下一张表
		String tableNameB = getNextTableNameByCreateTime(createTime);
		// 自动清除告警的编号,格式为 ( 0.serialno )
		String ackserialno = "0." + serialNo;
		LOG.debug("tableNameA = " + tableNameA + " & tableNameB = "
				+ tableNameB + " & ackserialno = " + ackserialno);
		List list = warnDetailInfoDao.getLastFilterCreateTime(ackserialno,
				gatherId, tableNameA, tableNameB);

		if (list == null || list.size() == 0)
			return null;
		Map map = (Map) list.get(0);
		if (map == null)
			return null;

		long lastFilterCreateTime = ((BigDecimal) map
				.get("lastfiltercreatetime")).longValue();
		DateTimeUtil dateUtil = new DateTimeUtil(lastFilterCreateTime * 1000);
		String lastFilterCreateTimeStr = dateUtil.getLongDate();
		LOG.debug("lastFilterCreateTime = " + lastFilterCreateTime + " % "
				+ lastFilterCreateTimeStr);
		return lastFilterCreateTimeStr;
	}

	/**
	 * 获取CreatorType的描述信息
	 * 
	 * @param i
	 * @return
	 */
	private String getCreatorType(int i)
	{
		String creatorType = null;
		switch (i)
		{
		case 1:
			creatorType = "主机告警";
			break;
		case 2:
			creatorType = "PMEE告警";
			break;
		case 3:
			creatorType = "Syslog告警";
			break;
		case 4:
			creatorType = "Trap告警";
			break;
		case 5:
			creatorType = "规则引擎";
			break;
		case 6:
			creatorType = "Topo告警";
			break;
		case 7:
			creatorType = "业务告警";
			break;
		case 8:
			creatorType = "Ping检测设备通断";
			break;
		case 9:
			creatorType = "华为设备端口检查";
			break;
		case 10:
			creatorType = "Visualman";
			break;
		case 20:
			creatorType = "亚信告警";
			break;
		case 21:
			creatorType = "短信告警";
			break;
		default:
			creatorType = "未知告警";
		}
		return creatorType;
	}

	/**
	 * 测试用方法,苦于数据库暂时没有数据
	 * 
	 * @return
	 */
	private List getMyTestSysLogList()
	{
		int len = Double.valueOf(Math.random() * 10).intValue();
		Map map;
		List list = new ArrayList(len);
		for (int i = 0; i < len; i++)
		{
			map = new HashMap();
			map.put("file_name", "file_name" + i);
			map.put("file_desc", "file_desc" + i);
			map.put("file_size", 100);
			// 时间
			map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date(System.currentTimeMillis())));
			map.put("inner_url", "inner_url" + i);
			map.put("server_dir", "server_dir" + i);
			list.add(map);
		}
		return list;
	}
}
