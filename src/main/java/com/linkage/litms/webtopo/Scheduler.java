/*
 * @(#)Scheduler.java	1.00 12/31/2005
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.webtopo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AlarmEvent;
import RemoteDB.AlarmNum;
import RemoteDB.DeviceSearch;
import RemoteDB.DragEvent;
import RemoteDB.GatherIDEvent;
import RemoteDB.WebPosition;
import RemoteDB.WebTopoManager;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.CorbaInstFactory;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * Web Topo综合调度，负责Topo图、告警、告警数量从MasterControl后台中的获取
 * 
 * @author Dolphin
 * @version 1.00, 12/31/2005
 * @since Liposs 2.1
 */
@SuppressWarnings("unchecked")
public class Scheduler {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(Scheduler.class);

	private static WebTopoManager wtInstance = null;

	private static ArrayList imageList = null;

	public static ArrayList getImageList() {
		if (imageList == null) {
			imageList = new ArrayList();
			String mysql = "select icon_name from  tp_devicetype_icon";
			PrepareSQL psql = new PrepareSQL(mysql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(mysql);
			Map map = cursor.getNext();
			String path = LipossGlobals.getLipossHome() + File.separator
					+ "webtopo" + File.separator + "images" + File.separator;
			logger.debug(path);
			ImageIcon image = null;
			while (map != null) {
				image = new ImageIcon(path + (String) map.get("icon_name"));
				imageList.add(image);
				map = cursor.getNext();
			}

		}
		return imageList;
	}

	/**
	 * 缺省构造函数，通过Corba接口工厂初始化Corba接口
	 * 
	 */
	public Scheduler() {
		if (wtInstance == null) {
			wtInstance = (WebTopoManager) CorbaInstFactory.factory("webtopo");
		}

	}

	/**
	 * 重新邦定WebTopo的Corba接口
	 * 
	 * @return 是否邦定成功：true 成功，false 失败
	 */
	public boolean rebind() {
		try {
			wtInstance = (WebTopoManager) CorbaInstFactory.factory("webtopo");
			if (wtInstance != null)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 第一次获取告警（最新的200条）
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @return 返回AlarmEvent结构数组
	 */
	public AlarmEvent[] getAllAlarm(String area_id, GatherIDEvent[] _gatherList) {
		AlarmEvent[] list = null;

		if (wtInstance != null) {
			try {

				logger.debug("hemc>>>" + list);
				list = wtInstance.getAllAlarm(area_id, _gatherList);
				logger.debug("hemc>>>" + list);
			} catch (Exception e) {
				e.printStackTrace();
				if (rebind()) {
					list = wtInstance.getAllAlarm(area_id, _gatherList);
				}
			}
		}
		logger.debug("hemc>>>" + list.length);
		return list;
	}

	/**
	 * 取得序列号大于指定序列号的告警信息
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @return 返回AlarmEvent结构数组
	 */
	public AlarmEvent[] getNewAlarm(String area_id, GatherIDEvent[] _gatherList) {
		AlarmEvent[] list = null;

		if (wtInstance != null) {
			try {
				list = wtInstance.getNewAlarm(area_id, _gatherList);
			} catch (Exception e) {
				if (rebind()) {
					list = wtInstance.getNewAlarm(area_id, _gatherList);
				}
			}
		}
		return list;
	}

	/**
	 * 获取具体层所有对象的告警数量
	 * 
	 * @param arr_id
	 *            对象ID数组
	 * @return 返回AlarmNum结构数组
	 */
	public AlarmNum[] getAllAlarmNum(String area_id, String[] arr_id) {
		AlarmNum[] list = null;

		if (wtInstance != null) {
			try {
				list = wtInstance.getAllAlarmNum(area_id, arr_id);
			} catch (Exception e) {
				if (rebind()) {
					list = wtInstance.getAllAlarmNum(area_id, arr_id);
				}
			}
		}

		return list;
	}

	/**
	 * 获取具体层所有对象的告警数量 AlarmNum.level值定义如下 0：不在线 蓝色 1：在线 绿色 （图片本身的颜色，不需要单独制作图片）
	 * 2：暂停 黄色 3：停机（销户） 灰色
	 * 
	 * @param arr_id
	 *            对象ID数组
	 * @return 返回AlarmNum结构数组
	 */
	public AlarmNum[] getCurrentLayerDevStatus(String[] idList) {
		logger.debug("getCurrentLayerDevStatus(idList)");

		AlarmNum[] list = null;

		if (wtInstance != null) {
			try {
				list = wtInstance.getCurrentLayerDevStatus(idList);
			} catch (Exception e) {
				if (rebind()) {
					list = wtInstance.getCurrentLayerDevStatus(idList);
				}
			}
		}

		return list;
	}

	/**
	 * 获取webtopo当前层次所有设备的状态值
	 * 
	 * @param idList
	 * @return
	 */
	public String getLayerDevStatus(String[] idList) {
		logger.debug("getLayerDevStatus(idList)");

		if (idList == null || idList.length == 0) {
			logger.debug("idList == null");
			return "";
		}

		String result = null;
		AlarmNum[] list = getCurrentLayerDevStatus(idList);
		if (list != null && list.length != 0) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < list.length; i++) {
				AlarmNum obj = list[i];
				sb.append(";").append(obj.id).append(",").append(obj.level);
			}

			result = sb.toString();
			if (result.length() > 0) {
				result = result.substring(1);
			}
		}

		return result == null ? "" : result;
	}

	/**
	 * 对象当前层拖动
	 * 
	 * @param s
	 *            JS串形式的DragEvent结构
	 */
	public void ModifyObjectsPosition(String s) {
		DragEvent drag = getDragEventByJs(s);

		if (wtInstance != null) {
			try {
				wtInstance.ModifyObjectsPosition(drag);
			} catch (Exception e) {
				if (rebind()) {
					wtInstance.ModifyObjectsPosition(drag);
				}
			}
		}

	}

	/**
	 * 对象拖动到某个子层
	 * 
	 * @param s
	 *            JS串形式的DragEvent结构
	 */
	public void ModifyObjectsLayer(String s) {
		DragEvent drag = getDragEventByJs(s);

		if (wtInstance != null) {
			try {
				wtInstance.ModifyObjectsLayer(drag);
			} catch (Exception e) {
				if (rebind()) {
					wtInstance.ModifyObjectsLayer(drag);
				}
			}
		}
	}

	/**
	 * 获取与id同一层的所有topo数据
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @param id
	 *            Topo层ID
	 * @return 与id同一层的所有topo数据字节数组
	 */
	public byte[] getSameStreamData(String area_id, String id) {
		byte[] byteData = null;

		if (wtInstance != null) {
			try {
				byteData = wtInstance.getSameStreamData(area_id, id);
			} catch (Exception e) {
				if (rebind()) {
					byteData = wtInstance.getSameStreamData(area_id, id);
				}
			}
		}
		return byteData;
	}

	public byte[] getObjectData(String objid) {
		byte[] byteData = null;

		if (wtInstance != null) {
			try {
				byteData = wtInstance.getObjectData(objid);
			} catch (Exception e) {
				if (rebind()) {
					byteData = wtInstance.getObjectData(objid);
				}
			}
		}
		return byteData;

	}

	/**
	 * 获取父ID为pid的所有topo数据
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @param pid
	 *            Topo层ID
	 * @return 父ID为pid的所有topo数据字节数组
	 */
	public byte[] getChildStreamData(String area_id, String pid) {
		byte[] byteData = null;

		if (wtInstance != null) {
			try {
				byteData = wtInstance.getChildStreamData(area_id, pid);
			} catch (Exception e) {
				if (rebind()) {
					byteData = wtInstance.getChildStreamData(area_id, pid);
				}
			}
		}

		return byteData;
	}

	/**
	 * Topo保存
	 * 
	 * @return 返回保存成功与否
	 */
	public int InformSaveTopo(int type) {
		logger.debug("type:" + type);
		int result = -1;

		if (wtInstance != null) {
			try {
				result = wtInstance.InformSaveTopo(type);
			} catch (Exception e) {
				e.printStackTrace();
				if (rebind()) {
					result = wtInstance.InformSaveTopo(type);
				}
			}
		}
		logger.debug("result:" + result);

		return result;
	}

	/**
	 * 将JS串解析成Corba接口中的 DragEvent结构
	 * 
	 * @param s
	 *            JS串形式的DragEvent结构
	 * @return 返回DragEvent结构
	 */
	private DragEvent getDragEventByJs(String s) {
		String[] arr = null, arr_tmp = null;
		DragEvent drag = null;
		if (s == null || s.length() == 0)
			return drag;

		drag = new DragEvent();
		arr = s.split(";");
		drag.pid = arr[1];
		drag.vid = arr[0];
		String tmp = arr[2];
		arr = tmp.split("@");
		// logger.debug(tmp);
		WebPosition[] pos = new WebPosition[arr.length];
		for (int i = 0; i < arr.length; i++) {
			// logger.debug(arr[i]);
			arr_tmp = arr[i].split(",");
			pos[i] = new WebPosition();
			pos[i].id = arr_tmp[0];
			pos[i].x = Integer.parseInt(arr_tmp[1]);
			pos[i].y = Integer.parseInt(arr_tmp[2]);
		}
		drag.list = pos;
		return drag;
	}

	/**
	 * 根据传入的值在webtopo中得到设备信息，用于webtopo上查找设备
	 * 
	 * @param _area_id
	 * @param type
	 * @param value
	 */
	public DeviceSearch[] getSearchDevice(String _area_id, short type,
			String value) {
		DeviceSearch[] deviceData = null;
		if (wtInstance != null) {
			try {
				deviceData = wtInstance.getSearchDevice(_area_id, type, value);
			} catch (Exception e) {
				if (rebind()) {
					deviceData = wtInstance.getSearchDevice(_area_id, type,
							value);
				}
			}
		}
		return deviceData;
	}

	public String[] getAlarmLinkMsg(String area_id, AlarmEvent[] alarmEvent) {
		String[] link = null;
		if (wtInstance != null) {
			try {
				link = wtInstance.getAlarmLinkMsg(area_id, alarmEvent);
			} catch (Exception e) {
				if (rebind()) {
					link = wtInstance.getAlarmLinkMsg(area_id, alarmEvent);
				}
			}
		}
		return link;
	}

	/**
	 * 导入拓扑图（web界面导入设备入库，通知MC加载数据到拓扑图中）
	 * 
	 * @param area_id
	 * @param device_id
	 *            设备id数组
	 * @param pid
	 *            拓扑图父id
	 * @return 返回成功导入到拓扑图中记录数
	 */
	public int importTopo(String area_id, String[] device_id, String pid,
			int x, int y) {
		int flag = 0;
		if (wtInstance != null) {
			try {
				flag = wtInstance.importTopo(area_id, device_id, pid, x, y);
			} catch (Exception e) {
				if (rebind()) {
					flag = wtInstance.importTopo(area_id, device_id, pid, x, y);
				}
			}
		}
		return flag;
	}

	/**
	 * 获取用户选中的网元拓扑图，包括网元之间的链路，以便webtopo能导出成拓扑图
	 * 
	 * @param pid
	 *            当前层父id
	 * @param idList
	 *            用户选中的网元id数组
	 * @return 拓扑图数据
	 */
	public byte[] getStreamDataByIds(String pid, String[] idList) {
		byte[] byteData = null;

		if (wtInstance != null) {
			try {
				byteData = wtInstance.getStreamDataByIds(pid, idList);
			} catch (Exception e) {
				if (rebind()) {
					byteData = wtInstance.getStreamDataByIds(pid, idList);
				}
			}
		}
		return byteData;
	}

	/**
	 * 取得清除告警信息
	 * 
	 * @param area_id
	 *            登录域字符串
	 * @param _gatherList
	 * @return 返回AlarmEvent结构数组
	 */
	public AlarmEvent[] getClearAlarm(String area_id,
			GatherIDEvent[] _gatherList) {
		AlarmEvent[] list = null;

		if (wtInstance != null) {
			try {
				list = wtInstance.getClearAlarm(area_id, _gatherList);
			} catch (Exception e) {
				if (rebind()) {
					list = wtInstance.getClearAlarm(area_id, _gatherList);
				}
			}
		}
		return list;
	}

	/**
	 * 从MC实时取所有设备的告警
	 * 
	 * @param area_id
	 * @param device_id
	 * @return
	 */
	public List getDeviceWarnFromMC(String device_id) {
		AlarmEvent[] alarmEvent = null;

		if (wtInstance != null) {
			try {
				alarmEvent = wtInstance.getDeviceAlarm(device_id);
			} catch (Exception e) {
				if (rebind()) {
					alarmEvent = wtInstance.getDeviceAlarm(device_id);
				}
			}
		}
		if (alarmEvent != null) {
			return Arrays.asList(alarmEvent);
		} else {
			return null;
		}
	}

	public static void main(String args[]) {
		Scheduler scheduler = new Scheduler();

		// byte[] data = scheduler.getSameStreamData("1","1/0");
		// String str = GZIPHandler.Decompress(data);
		// logger.debug(str);

		GatherIDEvent[] gatherStructArr = null;
		gatherStructArr = new GatherIDEvent[1];

		gatherStructArr[0] = new GatherIDEvent();
		gatherStructArr[0].gather_id = "12345";
		gatherStructArr[0].max_event_id = "0";

		AlarmEvent[] list = scheduler.getAllAlarm("0", gatherStructArr);
		// AlarmEvent[] list = scheduler.getNewAlarm("admin",29);
		// AlarmNum[] list = scheduler.getAllAlarmNum("admin",new
		// String[]{"1/192.168.28.228"});
		// logger.debug(list[0].number+" "+list[0].level);

		// String jsStr =
		// "1;1/0;1/192.168.28.123,305,147@1/192.168.28.20,265,47@1/192.168.28.0,95,199@1/1,181,136";
		// scheduler.ModifyObjectsPosition(jsStr);
		System.exit(0);
	}
}