/*
 * 
 * 创建日期 2006-1-17
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class AdslAct {
    private Cursor cursor = null;

    private Map fields = null;

    private String strSQL = null;

    /**
     * 插入新Adsl用户信息
     */
    private String m_AdslUserInfoAdd_SQL = "insert into cus_radiuscustomer (user_id,username,passwd,ipaddress,macaddress,basdevice_ip,basdevice_slot," +
    		"basdevice_port,vlanid,device_ip,device_slot,device_port,workid,cotno,service_set,realname,sex,address,city_id,office_id,zone_id,vipcardno," +
    		"contractno,linkman,adsl_card,adsl_dev,adsl_ser,isrepair,linkman_credno,linkphone,agent,agent_credno,agentphone,bandwidth,opendate,onlinedate," +
    		"pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,device_shelf,vpiid,vciid,adsl_hl,userline,basdevice_shelf," +
    		"basdevice_frame,device_frame,user_state) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    //private String m_CityList_SQL = "select city_id,city_name from tab_city order by city_id";
   // private String m_CityInfoWhere_SQL = "select city_id,city_name from tab_city where city_id='";
    /**
     * 查询Adsl用户资料
     */
    private String m_AdslUserInfo_SQL = "select user_id,username,adsl_card,device_ip,device_id,realname,basdevice_ip FROM cus_radiuscustomer ";

    /**
     * 根据指定用户ID删除用户
     */
    private String m_AdslUserDel_SQL = "delete from cus_radiuscustomer where user_id=";

    /**
     * 通过用户帐号查询用户是否存在
     */
    private String m_AdslUser_ByUserName_SQL = "select phonenumber from cus_radiuscustomer where username=?";

    /**
     * 通过用户电话查询用户是否存在
     */
    private String m_AdslUser_ByPhone_SQL = "select username from cus_radiuscustomer where userphone=?";

    /**
     * 提取所有采集机
     */
    private String m_GatherInfo_SQL = "select * from tab_process_desc";
    
    /**
     * 提取用户所属所有采集机
     */
    private String m_UserGatherInfo_SQL = "select * from tab_process_desc where gather_id in (?)";

    private PrepareSQL pSQL = null;;

    public AdslAct() {
        pSQL = new PrepareSQL();
    }

    /**
     * 获取所有采集机
     * 
     * @return Cursor
     */
    public Cursor getGatherInfo() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_GatherInfo_SQL = "select gather_id, descr from tab_process_desc";
        }
        PrepareSQL psql = new PrepareSQL(m_GatherInfo_SQL);
        psql.getSQL();
        return DataSetBean.getCursor(m_GatherInfo_SQL);
    }
    
    /**
     * 获取用户所属所有采集机 overload by YYS 2006-10-16
     * 
     * @return Cursor
     */
    public Cursor getGatherInfo(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        UserRes curUser = (UserRes) session.getAttribute("curUser");
        List gatherList = curUser.getUserProcesses();
        String gather_ids = StringUtils.weave(gatherList);
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_UserGatherInfo_SQL = "select gather_id, descr from tab_process_desc where gather_id in (?)";
        }
        pSQL.setSQL(m_UserGatherInfo_SQL);
        pSQL.setStringExt(1, gather_ids,false);
        
        return DataSetBean.getCursor(pSQL.getSQL());
    }

    /**
     * 获取选择框
     * 
     * @param flag
     * @param compare
     * @param rename
     * @return String
     */
    public String getGatherInfoForm(boolean flag, String compare, String rename) {
        cursor = getGatherInfo();
        String strResourceList = FormUtil.createListBox(cursor, "gather_id", "descr", flag, compare, rename);
        return strResourceList;
    }
    
    /**
     * 获取选择框 overload by YYS 2006-10-16
     * 
     * @param flag
     * @param compare
     * @param rename
     * @return String
     */
    public String getGatherInfoForm(boolean flag, String compare, String rename,HttpServletRequest request) {
        cursor = getGatherInfo(request);
        String strResourceList = FormUtil.createListBox(cursor, "gather_id", "descr", flag, compare, rename);
        return strResourceList;
    }

    /**
     * 根据用户用户帐号得到绑定电话
     * 
     * @param username
     * @return String
     */
    public String getAdslUserPhoneFromUserName(String username) {
        pSQL.setSQL(m_AdslUser_ByUserName_SQL);
        pSQL.setString(1, username);
        fields = DataSetBean.getRecord(pSQL.getSQL());
        return (fields != null) ? String.valueOf(fields.get("phonenumber")) : null;
    }

    /**
     * 根据用户电话得到用户帐号
     * 
     * @param username
     * @return String
     */
    public String getAdslUserNameFromUserPhone(String userphone) {
        pSQL.setSQL(m_AdslUser_ByPhone_SQL);
        pSQL.setString(1, userphone);
        fields = DataSetBean.getRecord(pSQL.getSQL());
        return (fields != null) ? String.valueOf(fields.get("username")) : null;
    }

    /**
     * 实现对Adsl用户的增、删、改功能
     * 
     * @param request
     * @return String
     */
    public String adslUserAct(HttpServletRequest request) {
        String strSQL = "";
        // String arrAddressSQL = null;
        String strMsg = null;
        String strAction = request.getParameter("action");
        if (strAction.equals("delete")) { //删除操作
            String str_userid = request.getParameter("user_id");
            strSQL = m_AdslUserDel_SQL + str_userid;
        } else {
            String str_username = request.getParameter("username");
            String str_passwd = request.getParameter("passwd");

            String str_basdevice_ip = request.getParameter("basdevice_ip");

            if (str_basdevice_ip.equals("0")) {
                str_basdevice_ip = "null";
            }
            String str_basdevice_slot = request.getParameter("basdevice_slot");
            String str_basdevice_port = request.getParameter("basdevice_port");
            String str_vlanid = request.getParameter("vlanid");
            // modify by 2004-1-8
            String str_vpiid = request.getParameter("vpiid");
            String str_vciid = request.getParameter("vciid");
            String str_adsl_hl = request.getParameter("adsl_hl");
            String str_userline = request.getParameter("userline");
            // end
            // modify by 2004-6-30
            String str_basdevice_shelf = request.getParameter("basdevice_shelf");
            String str_basdevice_frame = request.getParameter("basdevice_frame");
            String str_device_frame = request.getParameter("device_frame");
            // end
            // String str_hidipaddress = request.getParameter("ipaddress");
            String str_device_ip = request.getParameter("loopback_ip");
            String str_ipaddress = request.getParameter("ipaddress");
            String str_macaddress = request.getParameter("macaddress");
            String str_device_slot = request.getParameter("device_slot");
            String str_device_port = request.getParameter("device_port");
            String str_workid = request.getParameter("workid");
            String str_cotno = request.getParameter("cotno");
            String str_service_set = request.getParameter("service_set");
            String str_realname = request.getParameter("realname");
            String str_sex = request.getParameter("sex");
            String str_address = request.getParameter("address");
            String str_city_id = request.getParameter("city_id");
            String str_office_id = request.getParameter("office_id");
            String str_zone_id = request.getParameter("zone_id");
            String str_vipcardno = request.getParameter("vipcardno");
            String str_contractno = request.getParameter("contractno");
            String str_linkman = request.getParameter("linkman");
            String str_adsl_card = request.getParameter("adsl_card");
            String str_adsl_dev = request.getParameter("adsl_dev");
            String str_adsl_ser = request.getParameter("adsl_ser");
            String str_isrepair = request.getParameter("isrepair");
            String str_linkman_credno = request.getParameter("linkman_credno");
            String str_linkphone = request.getParameter("linkphone");
            String str_agent = request.getParameter("agent");
            String str_agent_credno = request.getParameter("agent_credno");
            String str_agentphone = request.getParameter("agentphone");
            String str_bandwidth = request.getParameter("bandwidth");
            String str_opendate = request.getParameter("hidopendate");
            String str_onlinedate = request.getParameter("hidonlinedate");
            String str_pausedate = request.getParameter("hidpausedate");
            String str_closedate = request.getParameter("hidclosedate");
            String str_updatetime = request.getParameter("hidupdatetime");
            String str_staff_id = request.getParameter("staff_id");
            String str_remark = request.getParameter("remark");
            String str_phonenumber = request.getParameter("phonenumber");
            String str_cableid = request.getParameter("cableid");
            String str_bwlevel = request.getParameter("bwlevel");
            String str_device_shelf = request.getParameter("device_shelf");

            if (str_bwlevel != null && str_bwlevel.length() > 0) {
                double d = Double.parseDouble(str_bwlevel) / 100;
                //double d = (double)(tmp/100);
                str_bwlevel = Double.toString(d);
            }
             
            if (strAction.equals("add")) {
                pSQL.setSQL(m_AdslUser_ByUserName_SQL);
                pSQL.setString(1, str_username);
                fields = DataSetBean.getRecord(pSQL.getSQL());
                if (fields != null) {
                    //strSQL = "";
                    strMsg = "该ADSL客户已经存在！";
                } else {
                    //取得新的userid
                    long userid = DataSetBean.getMaxId("cus_radiuscustomer", "user_id");
                    String str_userid = String.valueOf(userid);

                    strSQL = m_AdslUserInfoAdd_SQL + str_userid + ",'" + str_username + "','" + str_passwd + "','" + str_ipaddress + "','"
                            + str_macaddress + "','" + str_basdevice_ip + "'," + str_basdevice_slot + "," + str_basdevice_port + "," + str_vlanid
                            + ",'" + str_device_ip + "'," + str_device_slot + "," + str_device_port + ",'" + str_workid + "','" + str_cotno + "','"
                            + str_service_set + "','" + str_realname + "','" + str_sex + "','" + str_address + "','" + str_city_id + "','"
                            + str_office_id + "','" + str_zone_id + "','" + str_vipcardno + "','" + str_contractno + "','" + str_linkman + "','"
                            + str_adsl_card + "','" + str_adsl_dev + "','" + str_adsl_ser + "','" + str_isrepair + "','" + str_linkman_credno + "','"
                            + str_linkphone + "','" + str_agent + "','" + str_agent_credno + "','" + str_agentphone + "'," + str_bandwidth + ","
                            + str_opendate + "," + str_onlinedate + "," + str_pausedate + "," + str_closedate + "," + str_updatetime + ",'"
                            + str_staff_id + "','" + str_remark + "','" + str_phonenumber + "','" + str_cableid + "'," + str_bwlevel + ","
                            + str_device_shelf + ",'" + str_vpiid + "'," + str_vciid + ",'" + str_adsl_hl + "'," + str_userline + ","
                            + str_basdevice_slot + "," + str_basdevice_frame + "," + str_device_frame + ",'1')";
                    
                    strSQL = strSQL.replaceAll(",,", ",null,");
                    strSQL = strSQL.replaceAll(",\\)", ",null)");
                    strSQL = strSQL.replaceAll("'null'", "null");
                }
            } else {
                String old_username = request.getParameter("old_username");
                if (!old_username.equals(str_username)) {
                    // 判断是否已经存在
                    pSQL.setSQL(m_AdslUser_ByUserName_SQL);
                    pSQL.setString(1, str_username);
                    fields = DataSetBean.getRecord(pSQL.getSQL());
                    // strSQL = "select username from cus_radiuscustomer where
                    // username='" + str_username + "'";
                    if (fields != null)
                        strMsg = "该ADSL客户已经存在！";
                }
                if (strMsg == null) {
                    String str_userid = request.getParameter("user_id");
                    String user_state = request.getParameter("user_state");
                    strSQL = "update cus_radiuscustomer set username='" + str_username + "',passwd='" + str_passwd + "',ipaddress='" + str_ipaddress
                            + "',macaddress='" + str_macaddress + "',basdevice_ip='" + str_basdevice_ip + "',basdevice_slot=" + str_basdevice_slot
                            + ",basdevice_port=" + str_basdevice_port + ",vlanid=" + str_vlanid + ",device_ip='" + str_device_ip + "',device_slot="
                            + str_device_slot + ",device_port=" + str_device_port + ",workid='" + str_workid + "',cotno='" + str_cotno
                            + "',service_set='" + str_service_set + "',realname='" + str_realname + "',sex='" + str_sex + "',address='" + str_address
                            + "',city_id='" + str_city_id + "',office_id='" + str_office_id + "',zone_id='" + str_zone_id + "',vipcardno='"
                            + str_vipcardno + "',contractno='" + str_contractno + "',linkman='" + str_linkman + "',adsl_card='" + str_adsl_card
                            + "',adsl_dev='" + str_adsl_dev + "',adsl_ser='" + str_adsl_ser + "',isrepair='" + str_isrepair + "',linkman_credno='"
                            + str_linkman_credno + "',linkphone='" + str_linkphone + "',agent='" + str_agent + "',agent_credno='" + str_agent_credno
                            + "',agentphone='" + str_agentphone + "',bandwidth=" + str_bandwidth + ",opendate=" + str_opendate + ",onlinedate="
                            + str_onlinedate + ",pausedate=" + str_pausedate + ",closedate=" + str_closedate + ",updatetime=" + str_updatetime
                            + ",staff_id='" + str_staff_id + "',remark='" + str_remark + "',phonenumber='" + str_phonenumber + "',cableid='"
                            + str_cableid + "',bwlevel=" + str_bwlevel + ",device_shelf=" + str_device_shelf + ",vpiid='" + str_vpiid + "',vciid="
                            + str_vciid + ",adsl_hl='" + str_adsl_hl + "',userline=" + str_userline + ",basdevice_shelf=" + str_basdevice_shelf
                            + ",basdevice_frame=" + str_basdevice_frame + ",device_frame=" + str_device_frame + ",user_state='" + user_state
                            + "' where user_id=" + str_userid;

                    strSQL = strSQL.replaceAll("=,", "=null,");
                    strSQL = strSQL.replaceAll("= where", "=null where");
                    strSQL = strSQL.replaceAll("'null'", "null");
                }
            }
        }

        if (strMsg == null && !strSQL.equals("")) {
        	PrepareSQL psql = new PrepareSQL(strSQL);
            psql.getSQL();
            int iCode = DataSetBean.executeUpdate(strSQL);
            if (iCode > 0) {
                strMsg = "ADSL客户资源操作成功！";
            } else {
                strMsg = "ADSL客户资源操作失败，请返回重试或稍后再试！";
            }
        }

        return strMsg;
    }

    /**
     * 实现提取Adsl用户资料列表
     * 
     * @param request
     * @return ArrayList(strBar,cursor)
     */
    public ArrayList getAdslUserInfoList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();
        // String tmpSQL = "";

        HttpSession session = request.getSession();
        UserRes curUser = (UserRes) session.getAttribute("curUser");
//        ArrayList m_DevicesList = (ArrayList) curUser.getUserDevRes();
//        String m_MathchDevices = "''";
//        if (m_DevicesList.size() > 0) {
//            m_MathchDevices = "'" + m_DevicesList.get(0) + "'";
//        }
//        for (int k = 1; k < m_DevicesList.size(); k++) {
//            m_MathchDevices += ",'" + m_DevicesList.get(k) + "'";
//        }
//        // HttpSession session = request.getSession();
        // String role_name = (String) session.getAttribute("role_name");
        // if (!role_name.toLowerCase().equals("system") &&
        // !role_name.equals("系统管理员")) {
        // // 抽取同登录用户所属属地的工单
        // tmpSQL += " where city_id='" + session.getAttribute("per_city") +
        // "'";
        // }

        strSQL = m_AdslUserInfo_SQL+" where device_id in (select res_id from tab_gw_res_area where area_id="+curUser.getAreaId()+" and res_type=1)"
//        + " where"
//        + " device_id in(" + m_MathchDevices + ")" 
        + " ORDER BY user_id";
//        m_MathchDevices = null;
        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);
        QueryPage qryp = new QueryPage();
        qryp.initPage(strSQL, offset, pagelen);
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(strSQL);
        cursor = DataSetBean.getCursor(psql.getSQL(), offset, pagelen);
        
//      设备权限过滤
//        DeviceResDataFilter deviceFilter = new DeviceResDataFilter(cursor, "device_id");
//        cursor = (Cursor)deviceFilter.doFilter(curUser);

        list.add(cursor);

        return list;
    }

    /**
     * 获得条件查询Adsl用户信息列表
     * 
     * @param request
     * @return ArrayList(strBar,Cursor)
     */
    public ArrayList getAdslUserInfoQuery(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();

        String account = request.getParameter("account");
        String adslphone = request.getParameter("adslphone");
        String dslamip = request.getParameter("dslamip");
        String user_state = request.getParameter("user_state");

        boolean flag = false;
        String tmpSQL = "";
        HttpSession session = request.getSession();
        String role_name = (String) session.getAttribute("role_name");
        if (!role_name.toLowerCase().equals("system") && !role_name.equals("系统管理员")) {
            // 抽取同登录用户所属属地的工单
            tmpSQL += " where city_id='" + session.getAttribute("per_city") + "'";
        } else {
            tmpSQL += "where 1>0 ";
        }

        String strSQL = "select * FROM cus_radiuscustomer " + tmpSQL;

        if (request.getParameter("submit") != null) {
            account = account.trim();
            adslphone = adslphone.trim();
            dslamip = dslamip.trim();

            if (account.length() != 0) {
                strSQL += " and username = '" + account + "' ";
            }

            if (adslphone.length() != 0) {
                strSQL += " and phonenumber like '%" + adslphone + "%' ";
            }

            // join dslamip
            if (dslamip.length() != 0) {
                strSQL += " and device_ip like '%" + dslamip + "%' ";
            }

            if (!user_state.equals("-1")) {
                strSQL += " and user_state='" + user_state + "'";
            }
            // 表示本次检索条件是手工添加的
            flag = true;
        }

        strSQL += " ORDER BY user_id";

        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);

        // 进入循环中
        if (flag) {
            session.setAttribute("strSQL", strSQL);
            offset = 1;
        } else {
            strSQL = String.valueOf(session.getAttribute("strSQL"));
        }
        QueryPage qryp = new QueryPage();
        qryp.initPage(strSQL, offset, pagelen);
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(strSQL);
        psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
        list.add(cursor);

        return list;
    }

    /**
     * 通过设备内部ID查询设备IP
     * @param _deviceId
     * @return map
     * modify wangfeng 20060902
     */
    public Map getDeviceIPById(String _deviceId){
    	String strSQL = "";
    	if(_deviceId !=null){
    		strSQL = "select loopback_ip from tab_gw_device where device_id = " + "'" + _deviceId + "'";
    		PrepareSQL psql = new PrepareSQL(strSQL);
            psql.getSQL();
    		Map resDS = DataSetBean.getRecord(strSQL);
    		return resDS;
    	}else{
    		return null;
    	}
    	
    }
    
    /**
     * 通过设备IP查询设备内部ID
     * @param _deviceIP
     * @return string
     * modify wangfeng 20060902
     */
    public String getDeviceIdByIP(String _deviceIP){
    	String deviceId = null;
    	String strSQL = "";
    	if (_deviceIP !=null){
    		strSQL = "select device_id from tab_gw_device where loopback_ip = " + "'" + _deviceIP + "'";
    		PrepareSQL psql = new PrepareSQL(strSQL);
            psql.getSQL();
    		Map resDS = DataSetBean.getRecord(strSQL);
    		if(resDS != null){
    			deviceId = (String)resDS.get("device_id");			
    		}
    	}
    	return deviceId;
    }

    /**
     * 通过设备IP查询设备内部ID
     * 
     * @return map
     * modify wangfeng 20060908
     */
    public Map getIdByDeviceIP(){
    	Map deviceMap = new HashMap();
    	deviceMap.clear();
    	String strSQL = "";
    	strSQL = "select device_id,loopback_ip from tab_gw_device ";
    	PrepareSQL psql = new PrepareSQL(strSQL);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("loopback_ip"))) {
				deviceMap.put((String) fields.get("loopback_ip"), (String) fields
						.get("device_id"));
				fields = cursor.getNext();
			}
		}
    	return deviceMap; 	
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根

    }

}
