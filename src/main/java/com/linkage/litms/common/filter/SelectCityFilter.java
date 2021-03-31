package com.linkage.litms.common.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class SelectCityFilter {

	// 传递JSP参数(HttpServletRequest)
	private HttpServletRequest request;

	// 建立session对象
	private HttpSession session;

	// 用于获取登陆用户的信息
	private UserRes curUser;

	// 建立cursor对象
	private Cursor cursor = null;

	// 用于获取登陆用户对应属地编号
	private String city_id = null;

	// 获取所有属地编号
	private String CityIDSQL = "select city_id from tab_city";

	// 获取属地的名称
	private String Name_CitySQL = "select city_name from tab_city where city_id = ?";

	// 获取属地的父编号
	private String Pid_CitySQL = "select parent_id from tab_city where city_id = ?";

	// 获取子属地
	private String GetIDByPid_CitySQL = "select city_id,city_name from tab_city where parent_id=?";

	private String S_CitySQL = "select city_id,city_name,parent_id from tab_city where parent_id = ? or city_id = ? order by city_id";

	// 取得省中心属地编号
	private String SZX_CityIDSQL = "select city_id from tab_city minus select a.city_id from tab_city a,tab_city b where a.parent_id=b.city_id";
	
	// 取得下一层属地
	private String Next_layer_CitySQL = "select city_id,city_name,parent_id from tab_city where parent_id = ? order by city_id";

	// 取得下属所有属地
	private String All_Sub_CitySQL = "select city_id,city_name,parent_id from tab_city where city_id in (?) order by city_id";

	public SelectCityFilter(HttpServletRequest request) {
		super();
		this.request = request;
		// TODO 自动生成构造函数存根
	}

	public SelectCityFilter() {
		super();
		// TODO 自动生成构造函数存根
	}

	/**
	 * 获取本身及下属所有属地的下拉框String 用于用户登陆时初始化CityFilter add by YYS
	 * 
	 * @param flag
	 *            是否showChild
	 * @param selected
	 *            生成下拉框中被默认选中的name值
	 * @param rename
	 *            生成下拉框名称
	 * @return String select 下拉框
	 */
	public String getSelfAndNextLayer(boolean flag, String selected,
			String rename) {

		session = request.getSession();
		curUser = (UserRes) session.getAttribute("curUser");
		city_id = curUser.getCityId();
		PrepareSQL pSQL;
		if(curUser.getUser().isAdmin()){
			pSQL = new PrepareSQL("select city_id,city_name,parent_id from tab_city");
		}else{
			if(Global.CQDX.equals(Global.instAreaShortName))
			{
				StringBuffer bf=new StringBuffer();
				bf.append("select city_id,city_name,parent_id from tab_city where 1=1 ");
				if(!StringUtil.IsEmpty(city_id))
				{
					String[] cityid=city_id.split(",");
					List list=new ArrayList();
					for(int i=0;i<cityid.length;i++){
						list.add(cityid[i]);
					}
					bf.append(" and parent_id in (");
					bf.append(StringUtils.weave(list)+" )");
					bf.append(" or city_id in ( ");
					bf.append(StringUtils.weave(list)+" ) order by city_id");
				}
				pSQL = new PrepareSQL(bf.toString());
			}else
			{
				pSQL = new PrepareSQL(S_CitySQL);
				pSQL.setString(1, city_id);
				pSQL.setString(2, city_id);
			}
			
		}
		
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		String strCityList = FormUtil.createListBox(cursor, "city_id",
				"city_name", flag, selected, rename);
		return strCityList;

	}

	/**
	 * 获取下属一层地市的下拉框String 用于生成用户所选属地的下一层属地 add by YYS
	 * 
	 * @param city_id
	 *            用户所选属地编号
	 * @param flag
	 *            是否showChild
	 * @param selected
	 *            生成下拉框中被默认选中的name值
	 * @param rename
	 *            生成下拉框名称
	 * @return String select 下拉框
	 */
	public String Next_layer_CitySQL(String city_id, boolean flag,
			String selected, String rename) {

		PrepareSQL pSQL = new PrepareSQL(Next_layer_CitySQL);
		pSQL.setString(1, city_id);
		cursor = DataSetBean.getCursor(pSQL.getSQL());

		String strCityList = FormUtil.createListBox(cursor, "city_id",
				"city_name", flag, selected, rename);
		return strCityList;

	}

	/**
	 * 获取下属所有地市的下拉框String 用于生成用户权限内所能看到的全部属地 add by YYS
	 * 
	 * @param city_id
	 *            用户所选属地编号
	 * @param flag
	 *            是否showChild
	 * @param selected
	 *            生成下拉框中被默认选中的name值
	 * @param rename
	 *            生成下拉框名称
	 * @param self
	 *            是否包含自身 true 包含 false 不包含
	 * @return String select 下拉框
	 */
	public String getAllSubCitiesBox(String city_id, boolean flag,
			String selected, String rename, boolean self) {

		PrepareSQL pSQL = new PrepareSQL(All_Sub_CitySQL);
		pSQL.setStringExt(1, getAllSubCityIds(city_id, self), false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());

		String strCityList = FormUtil.createListBox(cursor, "city_id",
				"city_name", flag, selected, rename);
		return strCityList;

	}

	/**
	 * 根据父属地ID获得所有下属子属地ID add by YYS
	 * 
	 * @return ArrayList
	 */
	public ArrayList getCityIdsByCityPid(ArrayList Pids, ArrayList TotalPids) {

		// ---------------------------");

		Cursor cursor = null;
		Map field = null;
		ArrayList tempCityIdList = new ArrayList();
		tempCityIdList.clear();

		for (int i = 0; i < Pids.size(); i++) {

			PrepareSQL pSQL = new PrepareSQL(GetIDByPid_CitySQL);
			pSQL.setString(1, (String) Pids.get(i));

			cursor = DataSetBean.getCursor(pSQL.getSQL());
			field = cursor.getNext();

			// 存在子节点 继续迭代
			if (field != null) {
				while (field != null) {

					// ----------------------------");
					tempCityIdList.add(field.get("city_id"));
					// city_id:"+field.get("city_id"));
					field = cursor.getNext();
				}

				// TotalPids.addAll(getCityIdsByCityPid(tempCityIdList,TotalPids));
				TotalPids = getCityIdsByCityPid(tempCityIdList, TotalPids);
				tempCityIdList.clear();

			} else {// 迭代终止 加入返回的ArrayList

				// ----------------------------");
				if (!TotalPids.contains(Pids.get(i))) {
					TotalPids.add(Pids.get(i));
					// city_id:"+Pids.get(i));
				}
			}
		}

		// 剔除重复对象
		// ArrayList temp_list = new ArrayList();
		// temp_list.clear();
		//
		// for (int s = 0; s < TotalPids.size(); s++) {
		// if (!temp_list.contains(TotalPids.get(s))) {
		// temp_list.add(TotalPids.get(s));
		// }
		// }
		// TotalPids.clear();
		// TotalPids = null;

		// return temp_list;

		// :"+StringUtils.weave(TotalPids));
		return TotalPids;
	}  /**
     * 根据父属地ID获得所有下属子属地ID add by YYS
     * 
     * @param nameFlag true Name false city_id
     * @return ArrayList
     */
    public ArrayList getCityIdsByCityPid(ArrayList Pids, ArrayList TotalPids,boolean nameFlag) {

        // ---------------------------");

        Cursor cursor = null;
        Map field = null;
        ArrayList tempCityIdList = new ArrayList();
        tempCityIdList.clear();

        for (int i = 0; i < Pids.size(); i++) {

            PrepareSQL pSQL = new PrepareSQL(GetIDByPid_CitySQL);
            pSQL.setString(1, (String) Pids.get(i));

            cursor = DataSetBean.getCursor(pSQL.getSQL());
            field = cursor.getNext();

            // 存在子节点 继续迭代
            if (field != null) {
                while (field != null) {

                    // ----------------------------");
                    if(nameFlag)
                        tempCityIdList.add(field.get("city_name"));
                    else
                        tempCityIdList.add(field.get("city_id"));
                    // city_id:"+field.get("city_id"));
                    field = cursor.getNext();
                }

                TotalPids = getCityIdsByCityPid(tempCityIdList, TotalPids);
                tempCityIdList.clear();

            } else {// 迭代终止 加入返回的ArrayList

                // ----------------------------");
                if (!TotalPids.contains(Pids.get(i))) {
                    TotalPids.add(Pids.get(i));
                    // city_id:"+Pids.get(i));
                }
            }
        }
        return TotalPids;
    }

	/**
	 * 判断属地ID是否为省中心 add by YYS
	 * 
	 * @param m_CityPid
	 * @return String 通过,分隔的属地ID字符串
	 */
	public boolean ifSZX(String m_CityPid) {
		
		if(DBUtil.GetDB() == Global.DB_MYSQL) {
			SZX_CityIDSQL = "select city_id from tab_city where city_id not in (select a.city_id from tab_city a,tab_city b where a.parent_id=b.city_id)";
		}
		PrepareSQL pSQL = new PrepareSQL(SZX_CityIDSQL);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();

		if (field != null) {
			String city_id = (String) field.get("city_id");
			return m_CityPid.equals(city_id);
		}

		return false;
	}

	/**
	 * 根据属地ID自身获得下属所有属地ID add by YYS
	 * 
	 * @param m_CityPid
	 * @param self
	 *            是否包含自身 true 包含 false 不包含
	 * @return String 通过,分隔的属地ID字符串
	 */
	public String getAllSubCityIds(String m_CityPid, boolean self) {
		if (!ifSZX(m_CityPid)) {
			return StringUtils.weave(getAllSubCityIdsWithArray(m_CityPid,self));
		}
		return CityIDSQL;
	}
    
    /**
     * 根据属地ID自身获得下属所有属地ID add by YYS
     * 
     * @param m_CityPid
     * @param self
     *            是否包含自身 true 包含 false 不包含
     * @return ArrayList 属地ID
     */
    public ArrayList getAllSubCityIdsWithArray(String m_CityPid, boolean self) {
        // 定义保存属地以及下属所有属地对象的链表
        ArrayList m_CityPidList = new ArrayList();
        ArrayList m_CityIdList = new ArrayList();
        m_CityPidList.clear();
        m_CityIdList.clear();
        if(Global.CQDX.equals(Global.instAreaShortName))
		{
            String [] cityid=m_CityPid.split(",");
            for(int i=0;i<cityid.length;i++)
            {
            	 m_CityPidList.add(cityid[i]);	
            }
		}else{
			m_CityPidList.add(m_CityPid);	
		}
        // 得到所有下属属地对象链表
        m_CityIdList = getCityIdsByCityPid(m_CityPidList, m_CityIdList);
        if (self)
        	if(Global.CQDX.equals(Global.instAreaShortName))
    		{
                String [] cityid=m_CityPid.split(",");
                for(int i=0;i<cityid.length;i++)
                {
                	 m_CityPidList.add(cityid[i]);	
                }
    		}else{
    			m_CityPidList.add(m_CityPid);	
    		}

        return m_CityIdList;
    }
    
    /**
     * 根据属地ID自身获得下属所有属地ID add by YYS
     * 
     * @param m_CityPid
     * @param self
     *            是否包含自身 true 包含 false 不包含
     * @param nameFlag true name false city_id
     * @return ArrayList if(nameFlag)属地Name else属地ID
     */
    public ArrayList getAllSubCityIdsWithArray(String m_CityPid, boolean self,boolean nameFlag) {
        // 定义保存属地以及下属所有属地对象的链表
        ArrayList m_CityPidList = new ArrayList();
        ArrayList m_CityIdList = new ArrayList();
        m_CityPidList.clear();
        m_CityIdList.clear();
        m_CityPidList.add(m_CityPid);
        // 得到所有下属属地对象链表
        if (self)
            m_CityIdList.add(this.getNameByCity_id(m_CityPid));
        m_CityIdList.addAll(getCityIdsByCityPid(m_CityPidList, m_CityIdList,nameFlag));

        return m_CityIdList;
    }

	/**
	 * 根据city_id获取parent_id
	 * 
	 * @return
	 */
	public String getPidByCity_id(String city_id) {

		PrepareSQL pSQL = new PrepareSQL(Pid_CitySQL);
		pSQL.setString(1, city_id);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();

		if (fields != null)
			return (String) fields.get("parent_id");

		return "";

	}

	/**
	 * 根据city_id得到下级属地以及本身属地
	 * @param city_id
	 * @return 返回结果集
	 */
	public Cursor getAllSubCitySelf(String city_id) {
		PrepareSQL pSQL;
		if(Global.SDLT.equals(Global.instAreaShortName))
		{
			pSQL =new PrepareSQL("select city_id,city_name,parent_id from tab_city where parent_id in (?) or city_id in (?) order by city_id");
			String[] cityid=city_id.split(",");
			List list=new ArrayList();
			for(int i=0;i<cityid.length;i++){
				list.add(cityid[i]);
			}
			pSQL.setString(1, StringUtils.weave(list));
			pSQL.setString(2, StringUtils.weave(list));
		}else
		{
		    pSQL = new PrepareSQL(S_CitySQL);
			pSQL.setString(1, city_id);
			pSQL.setString(2, city_id);
		}
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	/**
	 * 根据city_id得到下级属地以及本身属地
	 * @param city_id
	 * @return "city_id","sub_city_id_1","sub_city_id_2"
	 * @author Yan.HaiJian, ID Card No.5126
	 * @date 2006-12-1
	 */
	public String getCitySubAndSelf(String city_id) {
		String tmp = null;
		Cursor cursor = this.getAllSubCitySelf(city_id);
		
		Map map = cursor.getNext();
		if(null != map) {
			tmp = "'" + (String)map.get("city_id") + "'";
			map = cursor.getNext();
			while(null != map) {
				tmp += ",'" + (String)map.get("city_id") + "'";
				map = cursor.getNext();
			}
		}
		
		return tmp;
	}

	/**
	 * 根据city_id获取city_name
	 * 
	 * @return
	 */
	public String getNameByCity_id(String city_id) {
		if(Global.CQDX.equals(Global.instAreaShortName))
		{
			StringBuffer bf=new StringBuffer();
			bf.append("select city_name from tab_city where 1=1 ");
			if(!StringUtil.IsEmpty(city_id))
			{
				String[] cityid=city_id.split(",");
				List list=new ArrayList();
				for(int i=0;i<cityid.length;i++)
				{
					list.add(cityid[i]);
				}
				bf.append(" and city_id in(");
				bf.append(StringUtils.weave(list));
				bf.append(")");
			}
			PrepareSQL pSQL = new PrepareSQL(bf.toString());
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			Map fields = cursor.getNext();

			if (fields != null)
				return (String) fields.get("city_name");

			return "";
		}else
		{
			PrepareSQL pSQL = new PrepareSQL(Name_CitySQL);
			pSQL.setString(1, city_id);
			cursor = DataSetBean.getCursor(pSQL.getSQL());
			Map fields = cursor.getNext();

			if (fields != null)
				return (String) fields.get("city_name");

			return "";
		}
	}

	/**
	 * is suzhou js.
	 * 
	 * @param _city_id
	 * @return
	 * @author yanhj
	 * @date 2006-10-20
	 */
	public static boolean isSZ(String _city_id) {
		String sql = "select city_id from tab_city where city_id=? and (parent_id='0200' or city_id='0200')";
		
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setString(1, _city_id);
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		
		if (null != map)
			return true;
		
        return false;
	}
	
	/**
	 * is wuxi.js.
	 * @author yanhj
	 * @date 2006-11-3
	 * @param _city_id
	 * @return
	 */
	public static boolean isWX(String _city_id) {
		String sql = "select city_id from tab_city where city_id=? and (parent_id in('0300','0800') or city_id='0300' or city_id='0800')";
		
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setString(1, _city_id);
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		
		if (null != map)
			return true;
		
        return false;
	}
    
    /**
     * 获取下属一层属地返回CheckBox
     * @return
     */
    public static String getCityCheckBox(String _city_id) {
        String sqlTxt = "select * from tab_city where parent_id='"+_city_id+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlTxt = "select city_id,city_name,staff_id,remark,parent_id from tab_city where parent_id='"+_city_id+"'";
		}
        StringBuffer CityCB = new StringBuffer();
        PrepareSQL pSQL = new PrepareSQL(sqlTxt);

        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        // int size=cursor.getRecordSize();

        int i = 0;
        Map fields = cursor.getNext();
        while (fields != null) {
            String city_id = (String) fields.get("CITY_ID".toLowerCase());
            String city_name = (String) fields.get("CITY_NAME".toLowerCase());
            CityCB.append("<input type='checkbox' name='city' value='").append(
                    city_id).append("'>").append(city_name);
            if ((i + 1) % 6 == 0) {
                CityCB.append("<br>");

            }
            i++;
            fields = cursor.getNext();
        }

        return CityCB.toString();
    }
    
    /**
     * 获取省的直属下属属地 返回CheckBox
     * @param flag 是否包含省中心
     * @return
     */
    public static String getSubCityCheckBox(boolean flag) {
        String sqlTxt = "select * from tab_city where parent_id=(" +
                "select city_id from tab_city minus select a.city_id from tab_city a,tab_city b where a.parent_id=b.city_id)";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlTxt = "select city_id,city_name,staff_id,remark,parent_id from tab_city where parent_id=(" +
					"select city_id from tab_city minus select a.city_id from tab_city a,tab_city b where a.parent_id=b.city_id)";
		}

        if(flag)
            sqlTxt += " or city_id=(select city_id from tab_city minus select a.city_id from tab_city a,tab_city b where a.parent_id=b.city_id)";
        StringBuffer CityCB = new StringBuffer();
        PrepareSQL pSQL = new PrepareSQL(sqlTxt);

        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        // int size=cursor.getRecordSize();

        int i = 0;
        Map fields = cursor.getNext();
        while (fields != null) {
            String city_id = (String) fields.get("CITY_ID".toLowerCase());
            String city_name = (String) fields.get("CITY_NAME".toLowerCase());
            CityCB.append("<input type='checkbox' name='city' value='").append(
                    city_id).append("'>").append(city_name);
            if ((i + 1) % 6 == 0) {
                CityCB.append("<br>");

            }
            i++;
            fields = cursor.getNext();
        }

        return CityCB.toString();
    }
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		SelectCityFilter sc = new SelectCityFilter();

		ArrayList m_CityPidList = new ArrayList();
		ArrayList m_CityIdList = new ArrayList();
		m_CityPidList.clear();
		m_CityIdList.clear();

		m_CityPidList.add("01");

		// 得到所有下属属地对象链表
		m_CityIdList = sc.getCityIdsByCityPid(m_CityPidList, m_CityIdList);


	}

}
