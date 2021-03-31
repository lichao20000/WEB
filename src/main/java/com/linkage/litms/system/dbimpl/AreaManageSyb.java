/*
 *
 * 创建日期 2006-2-6
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.Area;
import com.linkage.litms.system.AreaManager;
import com.linkage.module.gwms.Global;

public class AreaManageSyb implements AreaManager {
	private PrepareSQL pSQL = null;

	private Cursor cursor = null;

	private Map fields = null;

	/**
	 * 根据区域ID删除区域对象
	 */
	private String m_AreaInfoDel_SQL = "delete from tab_area where area_id=?";

	/**
	 * 根据区域ID删除区域范围内设备以及子区域对象设备
	 */
	private String m_AreaDevicesDel_SQL = "delete from tab_gw_res_area where area_id=? and res_type=1";

	/**
	 * 获取所有区域对象
	 */
	private String m_Areas_Cursor_SQL = "select * from tab_area";

	/**
	 * 获取所有区域对象
	 */
	private String m_Areas_SQL = "select * from tab_area where area_id=? or area_pid=?";

	/**
	 * 根据区域ID和层次获取所有满足条件的区域对象
	 */
	private String m_Areas_ByAreaIdAndLayer_SQL = "select * from tab_area where area_id=? and area_layer=?";

	/**
	 * 根据area_pid提取所有以此ID为父ID的区域对象
	 */
	private String m_Areas_ByAreaPId_SQL = "select * from tab_area where area_pid=?";

	/**
	 * 根据区域ID得到所有此区域范围内设备资源
	 */
	//private String m_Devices_ByAreaId_SQL = "select resource_type_id,gather_id,device_id,device_name from tab_gw_device where device_status=1 and device_id in (select res_id from tab_gw_res_area where res_type=? and area_id=?)";
	//private String m_Devices_ByAreaId_SQL = "select gather_id,device_id,oui,device_serialnumber from tab_gw_device a left join tab_gw_res_area b"
	//                                      + " on a.device_id =  b.res_id where a.device_status=1 and b.res_type=? and b.area_id = ?";
	private String m_Devices_ByAreaId_SQL = "select a.gather_id,a.device_id,a.oui,a.device_serialnumber from tab_gw_device a,tab_gw_res_area b "
	                                      + " where a.device_id = b.res_id and a.device_status=1 and b.res_type=? and b.area_id = ?";
	
	/**
	 * 根据区域ID得到所有此区域内尚未配置的设备资源(在父节点中有，但是在本身的节点中没有)
	 * Alter(not in) by xiaoxf
	 */
	private String m_Devices_ByAreaId_NO_SQL = "select resource_type_id,gather_id,device_id,device_name from "
			+"(select resource_type_id, gather_id,device_id,device_name from tab_gw_device where device_status=1 and device_id in (select res_id from tab_gw_res_area where res_type=? and area_id=?))a left join "
			+"(select res_id from tab_gw_res_area where res_type=? and area_id=?)b on a.device_id=b.res_id  where b.res_id is null";
	
	private String m_Devices_ByAreaId_NO_SQL_new = "select device_id,oui,device_serialnumber from "
		+"(select device_id,oui,device_serialnumber from tab_gw_device,tab_gw_res_area where device_status=1 and device_id=res_id and res_type=? and area_id=? ?)a left join "
		+"(select res_id from tab_gw_res_area where res_type=? and area_id=?)b on a.device_id=b.res_id  where b.res_id is null order by device_id";
	
	/**sql: the device list of be config.*/
	/*private String m_DeviceToBeConfig = "select a.res_id into #tab_1 from (select res_id from tab_gw_res_area where res_type=? and area_id=?) a left join"
		+ " (select res_id from tab_gw_res_area where res_type=? and area_id=?) b on a.res_id=b.res_id where b.res_id is null"
	    + " select device_id,oui,device_serialnumber from #tab_1 ,tab_gw_device  where device_status=1 and device_id=res_id"
	    + " ? order by device_id"
		+ " drop table #tab_1";*/
	private String m_DeviceToBeConfig="select res_id into #tab_1 from tab_gw_res_area where res_type=? and area_id=? and res_id not in(select res_id from tab_gw_res_area where res_type=? and area_id=?)"		
	    + " select device_id,oui,device_serialnumber from #tab_1 ,tab_gw_device  where device_status=1 and device_id=res_id"
	    + " ? order by device_id"
		+ " drop table #tab_1";
	/**
	 * 获取所有的设备并且在自己的区域内尚未配置的设备,该ｓｑｌ仅用于针对根域的设备付资源的时候才采用
	 * Alter(not in) by xiaoxf
	 */
	/*private String m_Devices_NO_SQL = "select gather_id,device_id,oui,device_serialnumber from "
		+"(select gather_id,device_id,oui,device_serialnumber from tab_gw_device where device_status=1 ?)a left join "
		+"(select res_id from tab_gw_res_area where res_type=? and area_id=?)b on a.device_id=b.res_id  where b.res_id is null order by device_id";*/
	private String m_Devices_NO_SQL="select gather_id,device_id,oui,device_serialnumber from tab_gw_device where device_status=1 ?"
		+"and device_id not in( select res_id from tab_gw_res_area where res_type=? and area_id=?)";

	/**
	 * 根据区域ID删除设备
	 */
	private String m_Devices_ByAreaIdDel_SQL = "delete from tab_gw_res_area where res_type=? and area_id=? and res_id not in(";
    /**
     * 用于删除下属区域的device
     * Alter(not in) by xiaoxf
     */
    private String m_DelDeviceOfSubArea = "delete from tab_gw_res_area where res_type=1 and area_id in (select area_id from tab_area where area_pid = ? )"
                                       + " and res_id not in (select res_id from tab_gw_res_area where res_type=1 and area_id = ?)";

	/**
	 * 根据父区域ID得到所有子区域ID
	 */
	private String m_AreaId_ByAreaPid_SQL = "select area_id from tab_area where area_pid=?";

	/**
	 * 获得以此区域ID为子ID的父区域ID
	 */
	private String m_AreaPid_ByAreaId_SQL = "select area_pid from tab_area where area_id=?";	
	

	public AreaManageSyb() {
		super();
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}
    /**
     * 据区域ID删除下属区域内设备 add by hemc 2006-10-23 
     * @param area_id 父区域
     * @return 
     */
	public boolean delDeviceOfAreaOther(String area_id){
        pSQL.setSQL(this.m_DelDeviceOfSubArea);
        pSQL.setStringExt(1, area_id,false); 
        pSQL.setStringExt(2, area_id,false);
        int count = DataSetBean.executeUpdate(pSQL.getSQL());
        
        return true; 
    }
	/**
	 * 根据区域ID删除区域内设备
	 * 
	 * @param m_AreaIdList
	 * @param m_DevicesArr
	 * @return boolean
	 */
	public boolean delDevicesOfAreaOther(ArrayList m_AreaIdList,
			String[] m_DevicesArr) {
		if (m_AreaIdList.size() == 0) {
			return true;
		}

		ArrayList list = new ArrayList();
		list.clear();

		// 为了解决m_DevicesArr字段长度太长，导致数据SQL超出数据库限制
		// 现在按照100长度来判断处理，超出部分以100倍数逐个处理
		boolean m_DevicesArr_Flag = false;
		if (m_DevicesArr.length > 100) {
			m_DevicesArr_Flag = true;
		}

		m_Devices_ByAreaIdDel_SQL += "?";
		if (!m_DevicesArr_Flag) {
			for (int s = 1; s < m_DevicesArr.length; s++) {
				m_Devices_ByAreaIdDel_SQL += ",?";
			}
		} else {
			for (int s = 1; s < 100; s++) {
				m_Devices_ByAreaIdDel_SQL += ",?";
			}
		}

		m_Devices_ByAreaIdDel_SQL += ")";

		if (!m_DevicesArr_Flag) {
			for (int i = 0; i < m_AreaIdList.size(); i++) {
				pSQL.setSQL(m_Devices_ByAreaIdDel_SQL);
				pSQL.setInt(1, 1);
				pSQL.setInt(2, Integer.parseInt(String.valueOf(m_AreaIdList
						.get(i))));
				for (int k = 0; k < m_DevicesArr.length; k++) {
					pSQL.setString(k + 3, m_DevicesArr[k]);
				}

				list.add(pSQL.getSQL());
			}
		} else {
			int p = 0;
			for (int i = 0; i < m_AreaIdList.size(); i++) {
				for (int x = 0; x < Math
						.round((m_DevicesArr.length + 50) / 100); x++) {

					pSQL.setSQL(m_Devices_ByAreaIdDel_SQL);
					pSQL.setInt(1, 1);
					pSQL.setInt(2, Integer.parseInt(String.valueOf(m_AreaIdList
							.get(i))));
					// for (int k = 0; k < m_DevicesArr.length; k++) {
					// pSQL.setString(k + 3, m_DevicesArr[k]);
					// }
					p = 0;
					while (p < 100 && x * 100 + p < m_DevicesArr.length) {
						pSQL.setString(p + 3, m_DevicesArr[x * 100 + p]);
						p++;
					}

					list.add(pSQL.getSQL());
				}
			}
		}

		int[] iCode = DataSetBean.doBatch(list);

		list.clear();
		list = null;

		return (iCode != null) ? true : false;
	}

	/**
	 * 根据父区域ID获得所有下属子区域ID
	 * 
	 * @param m_AreaPidList
	 * @param m_AreaIdList
	 * @return ArrayList
	 */
	public ArrayList getAreaIdsByAreaPid(ArrayList m_AreaPidList,
			ArrayList m_AreaIdList) {
		Cursor cursor = null;
		Map field = null;
		ArrayList tempAreaIdList = new ArrayList();
		tempAreaIdList.clear();

		for (int i = 0; i < m_AreaPidList.size(); i++) {
			pSQL.setSQL(m_AreaId_ByAreaPid_SQL);
			pSQL.setInt(1, Integer.parseInt(String
					.valueOf(m_AreaPidList.get(i))));

			cursor = DataSetBean.getCursor(pSQL.getSQL());
			field = cursor.getNext();

			// 存在子节点
			if (field != null) {
				while (field != null) {
					tempAreaIdList.add(field.get("area_id"));

					if (!m_AreaIdList.contains(field.get("area_id"))) {
						m_AreaIdList.add(field.get("area_id"));
					}

					field = cursor.getNext();
				}
				m_AreaIdList.addAll(getAreaIdsByAreaPid(tempAreaIdList,
						m_AreaIdList));

				tempAreaIdList.clear();
				// tempAreaIdList = null;
			} else {
				m_AreaIdList.add(m_AreaPidList.get(i));
			}
		}
		// 剔除重复对象
		ArrayList temp_list = new ArrayList();
		temp_list.clear();

		for (int s = 0; s < m_AreaIdList.size(); s++) {
			if (!temp_list.contains(m_AreaIdList.get(s))) {
				temp_list.add(m_AreaIdList.get(s));
			}
		}
		m_AreaIdList.clear();
		m_AreaIdList = null;

		return temp_list;
	}

	/**
	 * 根据资源类型和区域ID得到所有范围内资源
	 * 
	 * @param res_type
	 * @param area_id
	 * @return Cursor
	 */
	public Cursor getDevicesOfAreaId(String res_type, String area_id) {
		return getDevicesOfAreaId(Integer.parseInt(res_type), Integer
				.parseInt(area_id));
	}

	/**
	 * 根据域ｉｄ获取所有的尚未分配权限的设备
	 * 
	 * @param res_type
	 * @param area_id
	 * @return
	 */
	public Cursor getOtherAllDevices(String res_type, String area_id,
			List gather_id,String device_type_id,String cityid,String ifcontainChild) {

		
		//属地过滤
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			cityid = scf.getAllSubCityIds(cityid, true);
		} else {
			cityid = "'" + cityid + "'";
		}
		
		String tem_s = "";		
		//添加设备类型过滤条件
		if(!device_type_id.equals("-1")) {
			tem_s = " and devicetype_id="+device_type_id;
		}
		//添加属地过滤条件
		tem_s += " and city_id in("+cityid+")";
		//添加采集点过滤条件
		if(gather_id.size()>0)
			tem_s += " and gather_id in("+StringUtils.weave(gather_id)+") ";
		
		
		pSQL.setSQL(m_Devices_NO_SQL);
		pSQL.setStringExt(1, tem_s, false);
		pSQL.setStringExt(2, res_type, false);
		pSQL.setStringExt(3, area_id, false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 获取父域已有的设备但是还没有给子域付的设备
	 * 
	 * @param res_type
	 * @param area_id
	 * @return
	 */
	public Cursor DevicesFilterByTypeAndGather(String res_type,
			String p_area_id, String area_id, String gather_id,
			String resource_type_id) {

		if (!gather_id.equals("-1")) {// 采集机过滤
			m_Devices_ByAreaId_NO_SQL += " and gather_id = '" + gather_id + "'";
		}
		if (!resource_type_id.equals("-1")) {// 设备类型过滤
			m_Devices_ByAreaId_NO_SQL += " and resource_type_id = "
					+ resource_type_id;
		}

		pSQL.setSQL(m_Devices_ByAreaId_NO_SQL);
		pSQL.setStringExt(1, res_type, false);
		pSQL.setStringExt(2, p_area_id, false);
		pSQL.setStringExt(3, res_type, false);
		pSQL.setStringExt(4, area_id, false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 获取父域已有的设备但是还没有给子域付的设备
	 * 
	 * @param res_type
	 * @param area_id
	 * @param curr_area_id 当前用户area_id
	 * @return
	 * @author yanhj
	 * @date 2006-11-8
	 */
	public Cursor getOtherDevicesOfAreaIdNew(String res_type, String area_id,
			List gather_id, String curr_area_id, String device_type_id,String cityid,String ifcontainChild) {
		
		
		//属地过滤
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			cityid = scf.getAllSubCityIds(cityid, true);
		} else {
			cityid = "'" + cityid + "'";
		}
		
		String tem_s = "";		
		//添加设备类型过滤条件
		if(!device_type_id.equals("-1")) {
			tem_s = " and devicetype_id="+device_type_id;
		}
		//添加属地过滤条件
		tem_s += " and city_id in("+cityid+")";
		//添加采集点过滤条件
		if(gather_id.size()>0)
			tem_s += " and gather_id in("+StringUtils.weave(gather_id)+") ";
		
		pSQL.setSQL(m_Devices_ByAreaId_NO_SQL_new);
		pSQL.setStringExt(1, res_type, false);
		pSQL.setStringExt(2, curr_area_id, false);		
		pSQL.setStringExt(3, tem_s, false);		
		pSQL.setStringExt(4, res_type, false);
		pSQL.setStringExt(5, area_id, false);
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	/**
	 * get the list of device which is to be configed.
	 * @param res_type 
	 * @param area_id: the aera_id of the area which is to be configed.
	 * @param gather_id
	 * @param curr_area_id: the area_id of current web_user.
	 * @return
	 * @author yanhj
	 * @date 2006-11-8
	 */
	public Cursor getToConfigDevice(String res_type, String area_id,
			List gather_id, String curr_area_id, String device_type_id,String cityid,String ifcontainChild) {
		
		//属地过滤
		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			cityid = scf.getAllSubCityIds(cityid, true);
		} else {
			cityid = "'" + cityid + "'";
		}
		
		String tem_s = "";		
		//添加设备类型过滤条件
		if(!device_type_id.equals("-1")) {
			tem_s = " and devicetype_id="+device_type_id;
		}
		//添加属地过滤条件
		tem_s += " and city_id in("+cityid+")";
		//添加采集点过滤条件
		if(gather_id.size()>0)
			tem_s += " and gather_id in("+StringUtils.weave(gather_id)+") ";
		
		
		pSQL.setSQL(m_DeviceToBeConfig);
		pSQL.setStringExt(1, res_type, false);
		pSQL.setStringExt(2, curr_area_id, false);
		pSQL.setStringExt(3, res_type, false);
		pSQL.setStringExt(4, area_id, false);
		pSQL.setStringExt(5, tem_s, false);
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}


	/**
	 * 根据资源类型和区域ID得到所有范围内资源
	 * 
	 * @param res_type
	 * @param area_id
	 * @return Cursor
	 */
	public Cursor getDevicesOfAreaId(int res_type, int area_id) {
		pSQL.setSQL(m_Devices_ByAreaId_SQL);
		pSQL.setInt(1, res_type);
		pSQL.setInt(2, area_id);

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	public Area createArea(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		Area area = this.getArea(m_AreaId);
		if (area == null) {
			area = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid, m_Area_RootId,
					m_AreaLayer, m_Acc_Oid, m_Remark);
		}

		return area;
	}

	public Area createArea(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark,
			ArrayList m_AreaDevices) {
		Area area = this.getArea(m_AreaId);
		if (area == null) {
			area = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid, m_Area_RootId,
					m_AreaLayer, m_Acc_Oid, m_Remark, m_AreaDevices);
		}

		return area;
	}

	public boolean delArea(int m_AreaId) {
		ArrayList list = new ArrayList();
		list.clear();
		pSQL.setSQL(m_AreaInfoDel_SQL);
		pSQL.setInt(1, m_AreaId);
		list.add(pSQL.getSQL());
		pSQL.setSQL(m_AreaDevicesDel_SQL);
		pSQL.setInt(1, m_AreaId);
		list.add(pSQL.getSQL());

		int[] iCode = DataSetBean.doBatch(list);

		return (iCode != null) ? true : false;
	}

	public boolean delArea(String m_AreaId) {
		return delArea(Integer.parseInt(m_AreaId));
	}

	public boolean delArea(Area m_Area) {
		return this.delArea(m_Area.getAreaId());
	}

	public Area getArea(int m_AreaId) {
		return new AreaSyb(m_AreaId);
	}

	public Area getArea(String m_AreaName) {
		return new AreaSyb(m_AreaName);
	}

	public boolean refAreaInfo(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark,
			ArrayList m_AreaDevices) {
		AreaSyb syb = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid,
				m_Area_RootId, m_AreaLayer, m_Acc_Oid, m_Remark, m_AreaDevices);
		boolean flag = syb.updateAreaInfoToDb();
		syb = null;

		return flag;
	}

	public boolean refAreaInfo(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		AreaSyb syb = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid,
				m_Area_RootId, m_AreaLayer, m_Acc_Oid, m_Remark);
		boolean flag = syb.updateAreaInfoToDb();
		syb = null;

		return flag;
	}

	/**
	 * 根据区域ID和范围内设备刷新区域
	 * 
	 * @param m_AreaId
	 * @param m_AreaDevices
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, ArrayList m_AreaDevices) {
		boolean flag = false;
		Area m_Area = this.getArea(m_AreaId);
		m_Area.setAreaDevices(m_AreaDevices);
		flag = this.refAreaInfo(m_Area);
		m_Area = null;

		return flag;
	}

	/**
	 * 根据区域ID和范围内设备刷新区域
	 * 
	 * @param m_AreaId
	 * @param m_AreaDevicesArr
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, String[] m_AreaDevicesArr) {
		ArrayList m_AreaDevices = new ArrayList();
		m_AreaDevices.clear();
		for (int i = 0; i < m_AreaDevicesArr.length; i++) {
			m_AreaDevices.add(m_AreaDevicesArr[i]);
		}

		return refAreaInfo(m_AreaId, m_AreaDevices);
	}

	/**
	 * 根据区域ID和范围内设备刷新区域
	 * 
	 * @param m_AreaId
	 * @param m_AreaDevicesArr
	 * @return boolean
	 */
	public boolean refAreaInfo(String m_AreaId, String[] m_AreaDevicesArr) {
		return refAreaInfo(Integer.parseInt(m_AreaId), m_AreaDevicesArr);
	}

	/**
	 * 刷新区域信息
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		AreaSyb syb = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid,
				m_AreaLayer, m_Acc_Oid, m_Remark);
		boolean flag = syb.updateAreaInfoToDb();
		syb = null;

		return flag;
	}

	/**
	 * 刷新区域信息
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @return boolean
	 */
	public boolean refAreaInfo(int m_AreaId, String m_AreaName,
			int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		AreaSyb syb = new AreaSyb(m_AreaId, m_AreaName, m_AreaLayer, m_Acc_Oid,
				m_Remark);
		boolean flag = syb.updateAreaInfoToDb();
		syb = null;

		return flag;
	}

	public boolean refAreaInfo(Area m_Area) {
		AreaSyb syb = new AreaSyb(m_Area);
		boolean flag = syb.updateAreaInfoToDb();
		syb = null;

		return flag;
	}

	/**
	 * 返回指定起点并顺延numResults个角色对象
	 * 
	 * @param startIndex
	 * @param numResults
	 * @return Cursor
	 */
	public Cursor areas(int startIndex, int numResults) {
		Cursor temp_cursor = new Cursor();
		cursor = getAreaInfoAll();
		fields = cursor.getNext();

		int mid = 0;

		// 到达startIndex
		while (fields != null && mid < startIndex) {
			mid++;

			fields = cursor.getNext();
		}

		mid = 0;
		// 提取数据
		while (fields != null && mid < numResults) {
			temp_cursor.add(fields);

			mid++;
			fields = cursor.getNext();
		}

		cursor = null;

		return temp_cursor;
	}

	/**
	 * 根据区域ID和层次提取属于此层次的所有满足条件的区域对象
	 * 
	 * @param m_AreaId
	 * @param m_Layer
	 * @return Cursor
	 */
	public Cursor getAreaInfosOfLayer(int m_AreaId, int m_Layer) {
		pSQL.setSQL(m_Areas_ByAreaIdAndLayer_SQL);
		pSQL.setInt(1, m_AreaId);
		pSQL.setInt(2, m_Layer);

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 根据区域ID提取以此ID为父ID的所有区域对象
	 * 
	 * @param m_AreaId
	 * @return Cursor
	 */
	public Cursor getAreaInfosOfAreaPId(int m_AreaId) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_Areas_ByAreaPId_SQL = "select area_id, area_name from tab_area where area_pid=?";
		}
		pSQL.setSQL(m_Areas_ByAreaPId_SQL);
		pSQL.setInt(1, m_AreaId);

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 根据区域ID提取以此ID为父ID的所有区域对象
	 * 
	 * @param m_AreaId
	 * @return Cursor
	 */
	public Cursor getAreaInfosOfAreaPId(String m_AreaId) {
		return getAreaInfosOfAreaPId(Integer.parseInt(m_AreaId));
	}

	/**
	 * 获取所有区域对象
	 * 
	 * @return Cursor
	 */
	public Cursor getAreaInfoAll() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_Areas_Cursor_SQL = "select area_id, area_layer, area_name, remark, area_pid from tab_area";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(m_Areas_Cursor_SQL);
		psql.getSQL();
		return cursor = DataSetBean.getCursor(m_Areas_Cursor_SQL);
	}	
	
	/**
	 * 获取所有区域对象ID
	 * 
	 * @return List
	 */
	public List getAreaIdAll() {
		Cursor cursor = getAreaInfoAll();
		List list = new ArrayList();

		Map field = cursor.getNext();
		while (field != null) {
			list.add(field.get("area_id"));

			field = cursor.getNext();
		}

		// clear
		field = null;
		cursor = null;

		return list;
	}
	
	/**
	 * 获取所有区域
	 * @return List<Map>
	 */
	public List<Map> getAreaAll() {
		Cursor cursor = getAreaInfoAll();
		List list = new ArrayList();
		
		Map map = new HashMap();
		
		Map field = cursor.getNext();
		while (field != null) {
			map.put("area_id", field.get("area_id"));
			map.put("area_name", field.get("area_name"));
			list.add(map);
			
			field = cursor.getNext();
		}
		
		// clear
		field = null;
		cursor = null;
		
		return list;
	}
	
	/**
	 * 根据id获取区域id以及下级区域
	 * @return List<Map>
	 */
	public List<Map> getAreaById(int area_id) {
		Cursor cursor = getCurrentArea(area_id);
		List list = new ArrayList();
		
		Map map = new HashMap();
		
		Map field = cursor.getNext();
		while (field != null) {
			map.put("area_id", field.get("area_id"));
			map.put("area_name", field.get("area_name"));
			list.add(map);
			
			field = cursor.getNext();
		}
		
		// clear
		field = null;
		cursor = null;
		
		return list;
	}

	public ArrayList getLowerAreaIds(int m_AreaId) {
		Cursor cursor = getAreaInfosOfAreaPId(m_AreaId);
		ArrayList list = new ArrayList();
		Map field = cursor.getNext();

		while (field != null) {
			list.add(field.get("area_id"));

			field = cursor.getNext();
		}

		// clear
		field = null;
		cursor = null;

		return list;
	}

	public ArrayList getLowerToFloorAreaIds(int m_AreaId) {
		ArrayList m_AreaPidList = new ArrayList();
		ArrayList m_AreaIdList = new ArrayList();

		m_AreaPidList.clear();
		m_AreaIdList.clear();

		m_AreaPidList.add(String.valueOf(m_AreaId));
		m_AreaIdList = getAreaIdsByAreaPid(m_AreaPidList, m_AreaIdList);

		// clear
		m_AreaPidList = null;

		return m_AreaIdList;
	}

	public int getUpperAreaIds(int m_AreaId) {
		pSQL.setSQL(m_AreaPid_ByAreaId_SQL);
		pSQL.setInt(1, m_AreaId);
		Map field = DataSetBean.getRecord(pSQL.getSQL());
        if(field != null){
            m_AreaId = Integer.parseInt(String.valueOf(field.get("area_pid")));
        }
		// clear
		field = null;

		return m_AreaId;
	}

	public ArrayList getUpperToTopAreaIds(int m_AreaId) {
		int present_AreaId = m_AreaId;
		ArrayList list = new ArrayList();
		list.clear();
		int num=1;
        boolean flag=true;
        int tmp=-10000;
		// 用来判断是否是已经到了最大域id
		// boolean flag = false;

		while (num<20&&flag) {
			
			m_AreaId = getUpperAreaIds(m_AreaId);

			// 表示是第一次，此区域本身不需要加入进来
			 if (m_AreaId==tmp) 
			 flag = false;
			 else {
                 //不在列表中，才存入列表
                 if(!list.contains(String.valueOf(m_AreaId)))
                     list.add(String.valueOf(m_AreaId));
			}
			num++;
			tmp=m_AreaId;
		}
		list.add(String.valueOf(present_AreaId));
		return list;
	}

	/**
	 * 更新区域信息
	 * 
	 * @return int 0 ok;1 重名;2 数据库错误
	 */
	public int refAreaInfoWithNameCheck(int m_AreaId, String m_AreaName,
			int m_Area_Pid, int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid,
			String m_Remark) {
		AreaSyb syb = new AreaSyb(m_AreaId, m_AreaName, m_Area_Pid,
				m_Area_RootId, m_AreaLayer, m_Acc_Oid, m_Remark);
		int flag = syb.updateAreaInfoWithNameCheck();
		syb = null;

		return flag;
	}

	/**
	 * get area and sub area of current user.
	 * 
	 * @author Yanhj
	 * @date
	 * 
	 * @param area_id
	 * @return
	 */
	public Cursor getCurrentArea(long _area_id) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_Areas_SQL = "select area_id, area_name from tab_area where area_id=? or area_pid=?";
		}

		pSQL.setSQL(m_Areas_SQL);
		pSQL.setLong(1, _area_id);
		pSQL.setLong(2, _area_id);

		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 根据area_id得到,包括自身以及所有的area_pid的List
	 * AreaManageSyb.java
	 * @param area_id
	 * @return
	 * List
	 * @author zhaixf
	 */
	public static List getAllareaPid(String area_id){
		List list = new ArrayList();
		//用于循环条件控制
		boolean flag = true;
		String strSql = "";
		Map areaMap = new HashMap();
		//把当前area_id加入list
		String curAreaId = area_id;
		list.add(curAreaId);
		while(flag){
			strSql = "select area_pid from tab_area where area_id = " + curAreaId;
			PrepareSQL psql = new PrepareSQL(strSql);
			areaMap = DataSetBean.getRecord(psql.getSQL());
			if(areaMap != null) {
				curAreaId = (String) areaMap.get("area_pid");
				if(curAreaId != null && !"".equals(curAreaId)){
					list.add(curAreaId);
				}
			}else{
				flag = false;
			}
		}
		return list;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		getAllareaPid("1");
	}
}
