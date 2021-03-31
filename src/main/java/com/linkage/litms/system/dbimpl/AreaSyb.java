/*
 * 
 * 创建日期 2006-1-24
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.Area;
import com.linkage.litms.system.AreaNotFoundException;
import com.linkage.module.gwms.Global;

/**
 * 用数据库的方式实现Area接口
 * 
 * @author suzr
 * @version 1.00, 2/6/2006
 * @since Liposs 2.1
 */
public class AreaSyb implements Area {
	private static Logger logger = LoggerFactory.getLogger(AreaSyb.class);
	
	private static final int INSERT_OPER = 1;

	private static final int UPDATE_OPER = 2;

	private int m_AreaId;

	private String m_AreaName;

	private int m_Area_RootId;

	private String m_Remark;

	private int m_Area_Pid;

	private int m_Acc_Oid;

	private int m_AreaLayer;

	private Cursor cursor = null;

	private Map fields = null;

	private Map m_AreaInfo = null;

	// private Area m_Area = null;

	private PrepareSQL pSQL = null;

	private ArrayList m_AreaDevices = null;

	/**
	 * 插入区域对象
	 */
	private String m_AreaInfoAdd_SQL = "insert into tab_area (area_id,area_name,area_pid,area_rootid,area_layer,acc_oid,remark) values (?,?,?,?,?,?,?)";

	/**
	 * 刷新区域对象
	 */
	private String m_AreaInfoUpdate_SQL = "update tab_area set area_name=?,area_pid=?,area_rootid=?,area_layer=?,acc_oid=?,remark=? where area_id=?";

	/**
	 * 删除关联区域设备
	 */
	private String m_AreaDevicesDel_SQL = "delete from tab_gw_res_area where area_id=? and res_type=1";

	/**
	 * 添加区域关联设备对象
	 */
	private String m_AreaDevicesAdd_SQL = "insert into tab_gw_res_area (res_type,res_id,area_id) values (?,?,?)";

	/**
	 * 根据区域ID获取区域
	 */
	private String m_AreaInfo_ById_SQL = "select * from tab_area where area_id=?";

	/**
	 * 根据区域名称获取区域
	 */
	private String m_AreaInfo_ByAreaName_SQL = "select * from tab_area where area_name=?";
    
    /**
     * 根据区域名称获取区域
     */
    private String m_AreaInfo_ByAreaNameWithoutSelf_SQL = "select * from tab_area where area_name=? and area_id !=?";

	/**
	 * 根据区域ID获取所有相关设备信息
	 */
	private String m_AreaDevices_ById_SQL = "select res_id from tab_gw_res_area where area_id=?";

	public AreaSyb() {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	/**
	 * 构造带区域ID参数对象
	 * 
	 * @param m_AreaId
	 */
	public AreaSyb(int m_AreaId) {
		this.m_AreaId = m_AreaId;

		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		loadAreaInfo();
	}

	public AreaSyb(String m_AreaName) {
		this.m_AreaName = m_AreaName;

		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		loadAreaInfo(m_AreaName);
	}

	/**
	 * 构造带区域详细信息对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 * @param m_AreaDevices
	 */
	public AreaSyb(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark,
			ArrayList m_AreaDevices) {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		this.m_AreaId = m_AreaId;
		this.m_Area_Pid = m_Area_Pid;
		this.m_Area_RootId = m_Area_RootId;
		this.m_AreaLayer = m_AreaLayer;
		this.m_Acc_Oid = m_Acc_Oid;
		this.m_Remark = m_Remark;
		this.m_AreaDevices = m_AreaDevices;
	}

	/**
	 * 构造带区域详细信息对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_Area_RootId
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 */
	public AreaSyb(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_Area_RootId, int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		this.m_AreaId = m_AreaId;
		this.m_Area_Pid = m_Area_Pid;
		this.m_AreaName = m_AreaName;
		this.m_Area_RootId = m_Area_RootId;
		this.m_AreaLayer = m_AreaLayer;
		this.m_Acc_Oid = m_Acc_Oid;
		this.m_Remark = m_Remark;
	}

	/**
	 * 构造对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_Area_Pid
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 */
	public AreaSyb(int m_AreaId, String m_AreaName, int m_Area_Pid,
			int m_AreaLayer, int m_Acc_Oid, String m_Remark) {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		this.m_AreaId = m_AreaId;
		this.m_Area_Pid = m_Area_Pid;
		this.m_AreaName = m_AreaName;
		this.m_AreaLayer = m_AreaLayer;
		this.m_Acc_Oid = m_Acc_Oid;
		this.m_Remark = m_Remark;
	}

	/**
	 * 构造对象
	 * 
	 * @param m_AreaId
	 * @param m_AreaName
	 * @param m_AreaLayer
	 * @param m_Acc_Oid
	 * @param m_Remark
	 */
	public AreaSyb(int m_AreaId, String m_AreaName, int m_AreaLayer,
			int m_Acc_Oid, String m_Remark) {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		this.m_AreaId = m_AreaId;
		this.m_AreaName = m_AreaName;
		this.m_AreaLayer = m_AreaLayer;
		this.m_Acc_Oid = m_Acc_Oid;
		this.m_Remark = m_Remark;
	}

	public AreaSyb(Area m_Area) {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}

		this.m_AreaId = m_Area.getAreaId();
		this.m_AreaName = m_Area.getAreaName();
		this.m_Area_Pid = m_Area.getAreaPid();
		this.m_Area_RootId = m_Area.getAreaRootId();
		this.m_AreaLayer = m_Area.getAreaLayer();
		this.m_Acc_Oid = m_Area.getAccOid();
		this.m_Remark = m_Area.getRemark();
		this.m_AreaDevices = m_Area.getAreaDevices();
	}

	/**
	 * 载入区域详细信息
	 */
	private void loadAreaInfo(String m_AreaName) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_AreaInfo_ByAreaName_SQL = "select area_id, area_name, area_pid, remark, acc_oid, area_layer, area_rootid from tab_area where area_name=?";
		}
		pSQL.setSQL(m_AreaInfo_ByAreaName_SQL);
		pSQL.setString(1, m_AreaName);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
   
		if (fields != null) {
			this.m_AreaId = Integer.parseInt((String) fields.get("area_id"));
			this.m_AreaName = (String) fields.get("area_name");
			this.m_Area_Pid = Integer.parseInt((String) fields.get("area_pid"));
			this.m_Remark = (String) fields.get("remark");
			this.m_Acc_Oid = Integer.parseInt((String) fields.get("acc_oid"));
			this.m_AreaLayer = Integer.parseInt((String) fields
					.get("area_layer"));
			this.m_Area_RootId = Integer.parseInt((String) fields
					.get("area_rootid"));

			// 获得角色对应权限
			//loadAreaDevicesFromDb(); 
			fields.clear();
			fields = null;
		} else {
			try {
				throw new AreaNotFoundException("区域ID：" + m_AreaId
						+ " 在表（tab_area）中未发现");
			} catch (AreaNotFoundException e) {

			}
		}
	}

	/**
	 * 载入区域详细信息
	 */
	private void loadAreaInfo() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_AreaInfo_ById_SQL = "select area_id, area_name, area_pid, remark, acc_oid, area_layer, area_rootid from tab_area where area_id=?";
		}
		pSQL.setSQL(m_AreaInfo_ById_SQL);
		pSQL.setInt(1, m_AreaId);

		Map fields = DataSetBean.getRecord(pSQL.getSQL());

		if (fields != null) {
			this.m_AreaId = Integer.parseInt((String) fields.get("area_id"));
			this.m_AreaName = (String) fields.get("area_name");
			this.m_Area_Pid = Integer.parseInt((String) fields.get("area_pid"));
			this.m_Remark = (String) fields.get("remark");
			this.m_Acc_Oid = Integer.parseInt((String) fields.get("acc_oid"));
			this.m_AreaLayer = Integer.parseInt((String) fields
					.get("area_layer"));
			this.m_Area_RootId = Integer.parseInt((String) fields
					.get("area_rootid"));

			// 获得角色对应权限
			loadAreaDevicesFromDb();
		} else {
			try {
				throw new AreaNotFoundException("区域ID：" + m_AreaId
						+ " 在表（tab_area）中未发现");
			} catch (AreaNotFoundException e) {

			}
		}

		fields.clear();
		fields = null;
	}

	/**
	 * 获取区域范围内设备
	 */
	private void loadAreaDevicesFromDb() {
		ArrayList list = new ArrayList();
		list.clear();

		pSQL.setSQL(m_AreaDevices_ById_SQL);
		pSQL.setInt(1, m_AreaId);

		cursor = DataSetBean.getCursor(pSQL.getSQL());

		fields = cursor.getNext();
		while (fields != null) {
			list.add(fields.get("res_id".toUpperCase()));

			fields = cursor.getNext();
		}

		// 区域范围内设备
		m_AreaDevices = list;

		// fields.clear();
		fields = null;
		cursor = null;
	}

	/**
	 * 区域信息入库
	 * 
	 * @return boolean
	 */
	public boolean insertIntoDb() {
		ArrayList list = new ArrayList();
		list.clear();
		pSQL.setSQL(m_AreaInfoAdd_SQL);
		pSQL.setInt(1, m_AreaId);
		pSQL.setString(2, m_AreaName);
		pSQL.setInt(3, m_Area_Pid);
		pSQL.setInt(4, m_Area_RootId);
		pSQL.setInt(5, m_AreaLayer);
		pSQL.setInt(6, m_Acc_Oid);
		pSQL.setString(7, m_Remark);
		list.add(pSQL.getSQL());
		list.addAll(getInsertAreaDevicesOfSQL());

		Object[] arr_SQL = (Object[]) list.toArray();
		list.clear();
		int[] iCode = DataSetBean.doBatch(arr_SQL);

		return (iCode != null) ? true : false;
	}

	/**
	 * 获得区域范围设备创建SQL
	 * 
	 * @return ArrayList
	 */
	private ArrayList getInsertAreaDevicesOfSQL() {
		ArrayList list = new ArrayList();
		list.clear();

		if (m_AreaDevices == null)
			return list;
		// modify by lizhaojun /taa_gw_res_area表结构更改！
		//int m_DeviceId = this.getAreaDevicesMaxId();
		for (int k = 0; k < m_AreaDevices.size(); k++) {
			pSQL.setSQL(m_AreaDevicesAdd_SQL);

			//pSQL.setInt(1, m_DeviceId);
			pSQL.setInt(1, 1);// 资源类型
			pSQL.setInt(2, Integer.parseInt(String
					.valueOf(m_AreaDevices.get(k))));
			pSQL.setInt(3, m_AreaId);

			list.add(pSQL.getSQL());

			//m_DeviceId++;
		}

		return list;
	}

	/**
	 * 获取区域ID最大值的下一个
	 * 
	 * @return int
	 */
	public int getAreaMaxId() {
		return Integer.parseInt(String.valueOf(DataSetBean.getMaxId("tab_area",
				"area_id")));
	}

	/**
	 * 获取区域设备关联ID最大值的下一个
	 * 
	 * @return int
	 */
//	public int getAreaDevicesMaxId() {
//		return Integer.parseInt(String.valueOf(DataSetBean.getMaxId(
//				"tab_gw_res_area", "id")));
//	}

	/**
	 * update area info 
	 * 		<LI>AREA</LI>
	 * 		<LI>DEVICE</LI>
	 * @return boolean
	 * @author yanhj
	 * @date 2006-11-9
	 */
	public boolean updateAreaInfoToDb() {
		pSQL.setSQL(m_AreaInfoUpdate_SQL);
		pSQL.setString(1, m_AreaName);
		pSQL.setInt(2, m_Area_Pid);
		pSQL.setInt(3, m_Area_RootId);
		pSQL.setInt(4, m_AreaLayer);
		pSQL.setInt(5, m_Acc_Oid);
		pSQL.setString(6, m_Remark);
		pSQL.setInt(7, m_AreaId);
		ArrayList list = new ArrayList();
		list.add(pSQL.getSQL());
		list.addAll(getSaveAreaDevicesOfSQL(UPDATE_OPER));
		int i = 0;
		int[] iCode = new int[list.size()];
		ArrayList tmpList = new ArrayList();
		while(i < list.size()) {
			tmpList.add(list.get(i));
			if(i % 500 == 0) {
				iCode = DataSetBean.doBatch(tmpList);
				tmpList.clear();
				logger.debug("Yanhj>>>Info:更新数据库" + i +"条成功...");
			}
			i++;
		}
		if(tmpList.size() > 0) {
			iCode = DataSetBean.doBatch(tmpList);
			tmpList.clear();
			logger.debug("Yanhj>>>Info:更新数据库" + i +"条成功#OVER#");
		}
		tmpList = null;
		list.clear();
		list = null;

		return (iCode != null) ? true : false;
	}

	/**
	 * 保存编辑之后或者是创建新的区域范围内设备
	 * 
	 * @param type
	 * @return ArrayList
	 */
	private ArrayList getSaveAreaDevicesOfSQL(int type) {
		ArrayList list = new ArrayList();
		list.clear();

		if (m_AreaDevices == null)
			return list;

		//int m_DeviceId = this.getAreaDevicesMaxId();

		if (type == INSERT_OPER) {
			for (int k = 0; k < m_AreaDevices.size(); k++) {
				pSQL.setSQL(m_AreaDevicesAdd_SQL);

				//pSQL.setInt(1, m_DeviceId);
				pSQL.setInt(1, 1);// 资源类型
				pSQL.setString(2, String.valueOf(m_AreaDevices.get(k)));
				pSQL.setInt(3, m_AreaId);

				list.add(pSQL.getSQL());
				//m_DeviceId++;
			}
		} else if (type == UPDATE_OPER) {
			// 删除区域设备关联表中特定区域范围设备
			pSQL.setSQL(m_AreaDevicesDel_SQL);
			pSQL.setInt(1, m_AreaId);
			list.add(pSQL.getSQL());
			// 插入区域范围内设备
			for (int k = 0; k < m_AreaDevices.size(); k++) {
				pSQL.setSQL(m_AreaDevicesAdd_SQL);

				//pSQL.setInt(1, m_DeviceId);
				pSQL.setInt(1, 1);// 资源类型
				pSQL.setString(2, String.valueOf(m_AreaDevices.get(k)));
				pSQL.setInt(3, m_AreaId);

				list.add(pSQL.getSQL());

				//m_DeviceId++;
			}
		}

		return list;
	}

	public int getAreaId() {
		return m_AreaId;
	}

	public String getAreaName() {
		return m_AreaName;
	}

	public int getAreaPid() {
		return m_Area_Pid;
	}

	public int getAreaRootId() {
		return m_Area_RootId;
	}

	public int getAreaLayer() {
		return m_AreaLayer;
	}

	public int getAccOid() {
		return m_Acc_Oid;
	}

	public String getRemark() {
		return m_Remark;
	}

	public void setAreaId(int m_AreaId) {
		this.m_AreaId = m_AreaId;
	}

	public void setAreaName(String m_AreaName) {
		this.m_AreaName = m_AreaName;
	}

	public void setAreaPid(int m_Area_Pid) {
		this.m_Area_Pid = m_Area_Pid;
	}

	public void setAreaLayer(int m_AreaLayer) {
		this.m_AreaLayer = m_AreaLayer;
	}

	public void setAccOid(int m_Acc_Oid) {
		this.m_Acc_Oid = m_Acc_Oid;
	}

	public void setRemark(String m_remark) {
		this.m_Remark = m_remark;
	}

	public void setAreaInfo(Map m_AreaInfo) {
		this.m_AreaInfo = m_AreaInfo;
	}

	public Map getAreaInfo() {
		return m_AreaInfo;
	}

	public void setAreaDevices(ArrayList m_AreaDevices) {
		this.m_AreaDevices = m_AreaDevices;
	}

	public ArrayList getAreaDevices() {
		return m_AreaDevices;
	}
    
    public boolean IsTheNameExist(){
        boolean flag = true;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_AreaInfo_ByAreaName_SQL = "select area_id, area_name, area_pid, remark, acc_oid, area_layer, area_rootid from tab_area where area_name=?";
		}
        pSQL.setSQL(m_AreaInfo_ByAreaName_SQL);
        pSQL.setString(1, m_AreaName.trim());
        
        Map fields = DataSetBean.getRecord(pSQL.getSQL());
        flag = (fields==null?false:true);        
        
        return flag;
    }
    
    public boolean IsTheNameExistWithoutSelf(){
        boolean flag = true;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			m_AreaInfo_ByAreaNameWithoutSelf_SQL = "select area_id, area_name, area_pid, remark, acc_oid, area_layer, area_rootid from tab_area where area_name=? and area_id !=?";
		}
        pSQL.setSQL(m_AreaInfo_ByAreaNameWithoutSelf_SQL);
        pSQL.setString(1, m_AreaName);
        //pSQL.setInt(1, m_AreaId); It is may be wrong,modify by hemc on 2006-10-25
        pSQL.setInt(2, m_AreaId);
        Map fields = DataSetBean.getRecord(pSQL.getSQL());
        flag = (fields==null?false:true);        
        
        return flag;
    }
    
    /**
     * 更新区域资料
     * 
     * @return int 0 ok;1 重名;2 数据库错误
     */
    public int updateAreaInfoWithNameCheck() {
        if(IsTheNameExistWithoutSelf()){
            return 1;
        }
        //系统在更新tab_area的时候,原来的做法是将传过来的area_layer直接更新
        //下面的做法是:使用select a.area_layer+1 from tab_area a where a.area_id=?找到上属区域的area_layer,并将area_layer+1更新下属区域
        /**********************Add by hemc 2006-10-25*****************************/
        
        String sqlUpdate = null;
       // String db = StringUtils.getSubstrFun();
        //sybase数据库
//        if(db.equals("substring")){
            sqlUpdate = "update tab_area set area_name=?,area_pid=?,area_rootid=?,acc_oid=?,remark=?,"
                     + " area_layer = " + com.linkage.litms.common.util.DbUtil.getNullFunction("(select a.area_layer from tab_area a where a.area_id=?)", "0") + "+1 where area_id = ?";
//        }else{//oracle数据库
//            sqlUpdate = "update tab_area b set b.area_name=?,b.area_pid=?,b.area_rootid=?,b.acc_oid=?,b.remark=?,"
//                     + " b.area_layer =(select a.area_layer+1 from tab_area a where a.area_id=?) where b.area_id = ?";
//        }
        pSQL.setSQL(sqlUpdate);
        pSQL.setString(1, m_AreaName);
        pSQL.setInt(2, m_Area_Pid);
        pSQL.setInt(3, m_Area_RootId);
        pSQL.setInt(4, m_Acc_Oid);
        pSQL.setString(5, m_Remark);
        pSQL.setInt(6, m_Area_Pid);
        pSQL.setInt(7, m_AreaId);
        /************************************************************************/
        /*
        pSQL.setSQL(m_AreaInfoUpdate_SQL);
        pSQL.setString(1, m_AreaName);
        pSQL.setInt(2, m_Area_Pid);
        pSQL.setInt(3, m_Area_RootId);
        pSQL.setInt(4, m_AreaLayer);
        pSQL.setInt(5, m_Acc_Oid);
        pSQL.setString(6, m_Remark);
        pSQL.setInt(7, m_AreaId);
        */
        ArrayList list = new ArrayList();
        list.add(pSQL.getSQL());
        list.addAll(getSaveAreaDevicesOfSQL(UPDATE_OPER));

        int[] iCode = DataSetBean.doBatch(list);

        list.clear();
        list = null;
        
        return (iCode != null) ? 0 : 2;
    }

    /**
     * 区域信息入库
     * 
     * @return int 0 ok;1 重名;2 数据库错误
     */
    public int insertIntoDbWithNameCheck() {
        
        if(this.IsTheNameExist()){
            return 1;
        }          
        
        ArrayList list = new ArrayList();
        list.clear();
        pSQL.setSQL(m_AreaInfoAdd_SQL);
        pSQL.setInt(1, m_AreaId);
        pSQL.setString(2, m_AreaName);
        pSQL.setInt(3, m_Area_Pid);
        pSQL.setInt(4, m_Area_RootId);
        pSQL.setInt(5, m_AreaLayer);
        pSQL.setInt(6, m_Acc_Oid);
        pSQL.setString(7, m_Remark);
        list.add(pSQL.getSQL());
        list.addAll(getInsertAreaDevicesOfSQL());

        Object[] arr_SQL = (Object[]) list.toArray();
        list.clear();
        int[] iCode = DataSetBean.doBatch(arr_SQL);
        
        return (iCode != null) ? 0 : 2;
    }
    /**
     * 插入子区域 add by hemc 2006-10-26 (JSDXDKH-BUG-20061018-ZQX-001)
     * @return int 0 ok;1 重名;2 数据库错误
     */
    public int insertSubAreaWithNameCheck(){
        if(this.IsTheNameExist()){
            return 1;
        }
        //系统在更新tab_area的时候,原来的做法是将传过来的area_layer直接更新
        //下面的做法是:使用子查询的方法找到上属区域的area_layer,并将area_layer+1更新下属区域
        String sqlInsert = "insert into tab_area (area_id,area_name,area_pid,area_rootid,acc_oid,remark,area_layer)"
                     + " select ?,?,?,?,?,?,(a.area_layer+1) from tab_area a where a.area_id = ?";
        pSQL.setSQL(sqlInsert);
        pSQL.setInt(1, m_AreaId);
        pSQL.setString(2, m_AreaName);
        pSQL.setInt(3, m_Area_Pid);
        pSQL.setInt(4, m_Area_RootId);
        pSQL.setInt(5, m_Acc_Oid);
        pSQL.setString(6, m_Remark);
        pSQL.setInt(7, m_Area_Pid);
        int iCode = DataSetBean.executeUpdate(pSQL.getSQL());
        return (iCode > 0) ? 0 : 2;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// aa.indexOf(str, fromIndex)
	}

}
