package bio.webtopo.warn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;

import bio.webtopo.warn.column.ColumnInterface;
import bio.webtopo.warn.column.ColumnObject;
import bio.webtopo.warn.column._AckNumber;
import bio.webtopo.warn.column._AckTime;
import bio.webtopo.warn.column._ActiveStatus;
import bio.webtopo.warn.column._AlarmId;
import bio.webtopo.warn.column._Buss_id;
import bio.webtopo.warn.column._Buss_name;
import bio.webtopo.warn.column._CityColumn;
import bio.webtopo.warn.column._ClearStatus;
import bio.webtopo.warn.column._CreateTime;
import bio.webtopo.warn.column._CreatorName;
import bio.webtopo.warn.column._DeviceCoding;
import bio.webtopo.warn.column._DeviceType;
import bio.webtopo.warn.column._DisplayString;
import bio.webtopo.warn.column._DisplayTitle;
import bio.webtopo.warn.column._EventNo;
import bio.webtopo.warn.column._GatherID;
import bio.webtopo.warn.column._Number;
import bio.webtopo.warn.column._SegmentName;
import bio.webtopo.warn.column._Severity;
import bio.webtopo.warn.column._SourceIP;
import bio.webtopo.warn.column._SourceName;
import bio.webtopo.warn.column._StrOffice;
import bio.webtopo.warn.column._StrZone;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class ColumnInit {
	private static ColumnInit instance = new ColumnInit();
	private Map<String,ColumnInterface> MapColumn = null;

	public static ColumnInit getInstance() {
		return instance;
	}
	private ColumnInit() {
		reloadColumn();
	}
	/**
	 * 第一次打开告警牌获取有序列
	 * @return
	 */
	public Collection<ColumnInterface> getColumn(){
		return MapColumn.values();
	}
	/**
	 * 以后刷新告警时，根据列ｉｄ获取列对象
	 * @param columnID
	 * @return
	 */
	public Collection<ColumnInterface> getColumn(String[] columnID){
		List<ColumnInterface> list = new ArrayList<ColumnInterface>(columnID.length);
		ColumnInterface obj = null;
		for (int i = 0; i < columnID.length; i++) {
			obj = MapColumn.get(columnID[i]);
			if(obj != null){
				list.add(obj);
			}
		}
		return list;
	}
	/**
	 * 初始化列对象
	 */
	public boolean reloadColumn() {
		String sql = "select * from tab_warn_column where visible=1 order by sequence";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select field_name, field_desc, visible from tab_warn_column where visible=1 order by sequence";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		MapColumn = new LinkedHashMap<String, ColumnInterface>(cursor.getRecordSize());
		ColumnObject col = null;
		Map map = cursor.getNext();
		while (map != null) {
			SetMapColumnValue(map,col);
			map = cursor.getNext();
		}
		cursor=null;
		map=null;
		
		return true;
	}
	/**
	 * 给Map赋值
	 */
	private void SetMapColumnValue(Map map,ColumnObject col){
		if(map.get("field_name").equals("m_AlarmId")){//告警ID
			col = new _AlarmId((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		}else if (map.get("field_name").equals("m_CreateTime")) {// 告警创建时间
			col = new _CreateTime((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		}else if(map.get("field_name").equals("m_Severity")){//告警等级
			col = new _Severity((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		}else if (map.get("field_name").equals("m_CreatorName")) {// 创建事件网元的名称
			col = new _CreatorName((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_strDevType")) {// 设备型号
			col = new _DeviceType((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_SourceName")) {// 事件源网元的名称
			col = new _SourceName((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_SourceIP")) {// 事件源网元的IP地址
			col = new _SourceIP((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_AckNumber")) {// 确认的事件序列号
			col = new _AckNumber((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if(map.get("field_name").equals("buss_id")){//业务平台ID
			col = new _Buss_id((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if(map.get("field_name").equals("buss_name")){//业务平台名称
			col = new _Buss_name((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_ActiveStatus")) {// 告警状态
			col = new _ActiveStatus((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_DisplayTitle")) {// 事件显示的标题
			col = new _DisplayTitle((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_DisplayString")) {// 事件显示的内容
			col = new _DisplayString((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_GatherID")) {// 采集点ID
			col = new _GatherID((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_strCity")) {// 属地
			col = new _CityColumn((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_strOffice")) {// 局向
			col = new _StrOffice((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_strZone")) {// 小区
			col = new _StrZone((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_AckTime")) {// 确认时间
			col = new _AckTime((String) map.get("field_name"), (String) map
					.get("field_desc"), Boolean.parseBoolean((String) map
					.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("segmentName")) {// 告警所属云团名称(网络子图)
			col = new _SegmentName((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_DeviceCoding")) {// 设备ID
			col = new _DeviceCoding((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_Number")) {// 告警序列号
			col = new _Number((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		} else if (map.get("field_name").equals("m_EventNo")) {// 事件ID
			col = new _EventNo((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		}else if(map.get("field_name").equals("m_ClearStatus")){//清除状态
			col = new _ClearStatus((String) map.get("field_name"),
					(String) map.get("field_desc"), Boolean
							.parseBoolean((String) map.get("visible")));
			MapColumn.put(col.getId(),col);
		}
	}
}
