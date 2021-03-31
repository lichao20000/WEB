/*
 * ServeRatioSearch.java
 *
 * Created on 2006年1月19日, 下午2:10
 *
 * Copyright 2006 联创科技.版权所有
 */

package com.linkage.liposs.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.resource.ItvConfigBIO;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;

/**
 *
 * @author shenkejian
 * @version 1.00
 * @since Liposs 2.1
 */
public class ServeRatioSearch {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ItvConfigBIO.class);
    // 返回消息
    private String strMsg;
    
    // 登录用户资源信息 
    DbUserRes objUserRes;
    
    // 采集机列表
    private List gather_id;
    
    // 传递JSP参数(HttpServletRequest)
    private HttpServletRequest request;
    
    // 采集机编号、名称转换SQL
    private String GatherNamesql = "select * from tab_process_desc where gather_id in(?)";
    
    // 获得区域ID，名称SQL
    private String area_idSql = "select area_id from tab_area where area_pid in (?)";
    
    private String area_nameSQL = "Select * from tab_area where area_id in (?)";
    // 端到端业务设备信息SQL
    private String deviceinfosql = "select * from tab_device_info where gather_id in(?)";
    
    // 端到端业务ping包配置SQL
    private String PingPackagesql = "select * from tab_ping_packet_conf where gather_id in(?)";
    
    // 端到端业务ping任务配置表SQL
    
    private String PingTastsql = "select * from tab_ping_task_map where gather_id in(?) and user_type=?";
    
    private String PingTastsql2 = "select * from tab_ping_task_map where gather_id in(?)";
    
    // 端到端业务ping包配置、任务映射表SQL
    private String PingMapingsql = "select * from tab_ping_packet_task_map";
    
    // 查询被考核业务唯一标识SQL
    private String ServerSQL = "select task_name from tab_service_conf_map where task_type =? or task_type =?";
    
    /** 创建一下新的实例 ServeRatioSearch */
    public ServeRatioSearch(HttpServletRequest request) {
        super();
        this.request = request;
    }
    
    /**
     * 采集机编号、名称转换
     *
     *
     * @return
     */
    public Cursor GatherID() {
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        gather_id = objUserRes.getUserProcesses();

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            GatherNamesql = "select gather_id, descr, city_id, area_id from tab_process_desc where gather_id in(?)";
        }
        PrepareSQL pSQL = new PrepareSQL(GatherNamesql);
        pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        logger.debug("采集机编号和名称转换SQL:"+pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 获得改域下面所有子域
     * 
     * 
     */
    public Cursor area_id() {
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        long larea_id = objUserRes.getAreaId();
        ArrayList area_id = new ArrayList();
        area_id.add(String.valueOf(larea_id));
        String str_areaid = null;
        ArrayList result_area = new ArrayList();
        result_area.add(String.valueOf(larea_id));
        Cursor cursor = null;
        Map result = null;
        PrepareSQL pSQL = new PrepareSQL(area_idSql);
        
        for (int i=0; ;i++) {
            pSQL.setStringExt(1,this.weave(area_id),false);
            cursor = DataSetBean.getCursor(pSQL.getSQL());
            logger.debug("该域所有AreaID：" + pSQL.getSQL());
            area_id.clear();
            
            if (cursor.getRecordSize() == 0) {
          	 break;
            }else {
          	  result = cursor.getNext();
          	  
          	  while (result != null) {
          		  str_areaid = (String)result.get("area_id");
          		  area_id.add(str_areaid);
          		  result_area.add(str_areaid);
          		  result = cursor.getNext();
          	  }
          	  cursor = null;
            }
        }

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            area_nameSQL = "select area_id, area_name, area_pid, area_rootid, area_layer, acc_oid, remark from tab_area where area_id in (?)";
        }
        pSQL.setSQL(area_nameSQL);
        pSQL.setStringExt(1,this.weave(result_area),false);
        cursor = DataSetBean.getCursor(pSQL.getSQL());
        logger.debug("该域所有域名：" + pSQL.getSQL());
    	return cursor;
    }
    
	public String weave(List list) {
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append(list.get(0));
			for (int i = 1; i < list.size(); i++) {
				sb.append(",").append(list.get(i));
			}
		}

		return sb.toString();
	}
    /**
     * 端到端业务ping包配置、任务映射
     *
     *
     * @return cursor
     */
    public Cursor PingMaping() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            PingMapingsql = "select ping_packet_num, task_id, ping_packet_id from tab_ping_packet_task_map";
        }
        PrepareSQL pSQL= new PrepareSQL(PingMapingsql);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 添加ping任务配置与ping包的映射
     *
     *
     * @return strMsg
     */
    public String PringMapingAdd() {
        String taskid = request.getParameter("taskid");
        String pingid = request.getParameter("pingid");
        String pingNumber = request.getParameter("pingNumber");
        
        try {
            String sql = "select * from tab_ping_packet_task_map where task_id="
                    + taskid + " and ping_packet_id=" + pingid;
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                sql = "select ping_packet_num, task_id, ping_packet_id from tab_ping_packet_task_map where task_id="
                        + taskid + " and ping_packet_id=" + pingid;
            }
            PrepareSQL psql1= new PrepareSQL(sql);
            Cursor cursor = DataSetBean.getCursor(psql1.getSQL());
            Map fields = cursor.getNext();
            if (fields == null) {
                sql = "insert into tab_ping_packet_task_map values(" + taskid
                        + "," + pingid + "," + pingNumber + ")";
                PrepareSQL psql2= new PrepareSQL(sql);
                int row = DataSetBean.executeUpdate(psql2.getSQL());
                if (row > 0)
                    strMsg = "1";
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 删除ping任务配置与ping包的映射
     *
     *
     * @return strMsg
     */
    public String PingMapingDel() {
        String taskid = request.getParameter("taskid");
        String pingid = request.getParameter("pingid");
        
        try {
            String sql = "delete from tab_ping_packet_task_map where task_id="
                    + taskid + " and ping_packet_id=" + pingid;
            PrepareSQL psql = new PrepareSQL(sql);
            int row = DataSetBean.executeUpdate(psql.getSQL());
            if (row > 0)
                strMsg = "1";
            
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 修改ping任务配置与ping包的映射
     *
     *
     * @return strMsg
     */
    public String PingMapingModify() {
        String taskid = request.getParameter("taskid");
        String pingid = request.getParameter("pingid");
        String pingNumber = request.getParameter("pingNumber");
        
        try {
            String sql = "update tab_ping_packet_task_map set ping_packet_num="
                    + pingNumber + " where task_id=" + taskid
                    + " and ping_packet_id=" + pingid;
            PrepareSQL psql = new PrepareSQL(sql);
            int row = DataSetBean.executeUpdate(psql.getSQL());
            if (row > 0)
                strMsg = "1";
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 端到端业务设备信息列表
     *
     * @return cursor
     */
    public Cursor DeviceInfoList() {
        PrepareSQL pSQL;
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        gather_id = objUserRes.getUserProcesses();
        Cursor cursor = new Cursor();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            deviceinfosql = "select device_ip,device_company,device_name,device_desc,device_apanage,readcom,writecom,gather_id,telnet_user,telnet_passwd,telnet_enpasswd,telnet_model from tab_device_info where gather_id in(?)";
        }
        pSQL = new PrepareSQL(deviceinfosql);
        pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
        cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
   /**
     * 添加设备信息处理
     *Modyfied by suixz(5253) 2007-10-17
     *
     * @return
     */
    public String DeviceInfoAction() {
        String deviceip = request.getParameter("deviceip");
        String devicecompany = request.getParameter("devicecompany");
        String devicename = request.getParameter("devicename");
        String devicedescription = request.getParameter("devicedescription");
        String deviceapanage = request.getParameter("deviceapanage");
        String readcom = request.getParameter("readcom");
        String writecom = request.getParameter("writecom");
        String gather_id = request.getParameter("gather_id");
        String telnet_user = request.getParameter("telnet_user");
        String telnet_passwd = request.getParameter("telnet_passwd");
        String telnet_enpasswd = request.getParameter("telnet_enpasswd");
        String telnet_model = request.getParameter("telnet_model");
        strMsg = "0";
        try {
            String strSQL = "select * from tab_device_info where device_ip='"
                    + deviceip + "'";
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                strSQL = "select device_ip,device_company,device_name,device_desc,device_apanage,readcom,writecom,gather_id," +
                        "telnet_user,telnet_passwd,telnet_enpasswd,telnet_model from tab_device_info where device_ip='"
                        + deviceip + "'";
            }
            PrepareSQL psql = new PrepareSQL(strSQL);
            Cursor cursor = DataSetBean.getCursor(psql.getSQL());
            Map fields = cursor.getNext();
            if (fields != null) {
                strMsg = "0";
            } else {
                strSQL = "insert into tab_device_info(device_ip,device_company,device_name,device_desc,device_apanage,readcom,writecom,gather_id,telnet_user,telnet_passwd,telnet_enpasswd,telnet_model) values('" + deviceip
                        + "'," + devicecompany + ",'" + devicename + "','"
                        + devicedescription + "'," + deviceapanage + ",'"
                        + readcom + "','" + writecom + "','" + gather_id +"','"+telnet_user+"','"+telnet_passwd+"','"+telnet_enpasswd+"','"+telnet_model+ "')";
                PrepareSQL psql2 = new PrepareSQL(strSQL);
                int row = DataSetBean.executeUpdate(psql2.getSQL());
                if (row > 0)
                    strMsg = "1";
            }
            
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        
        return strMsg;
        
    }
    
    /**
     * 修改设备信息处理
     *
     *
     * @return strMsg
     */
    public String DeviceInfoModify() {
        String deviceip = request.getParameter("deviceip");
        String devicecompany = request.getParameter("devicecompany");
        String devicename = request.getParameter("devicename");
        String devicedescription = request.getParameter("devicedescription");
        String deviceapanage = request.getParameter("deviceapanage");
        String readcom = request.getParameter("readcom");
        String writecom = request.getParameter("writecom");
        String gather_id = request.getParameter("gather_id");
        strMsg = "0";
        try {
            String sql = "update tab_device_info set device_name='"
                    + devicename + "',device_company=" + devicecompany
                    + ",device_desc='" + devicedescription
                    + "',device_apanage=" + deviceapanage + ",readcom='"
                    + readcom + "',writecom='" + writecom + "',gather_id='"
                    + gather_id + "' where device_ip='" + deviceip + "'";
            PrepareSQL psql = new PrepareSQL(sql);
            int row = DataSetBean.executeUpdate(psql.getSQL());
            if (row > 0)
                strMsg = "1";
            
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 删除设备信息处理
     *
     *
     * @return strMsg
     */
    public String DeviceInfoDel() {
        String deviceip = request.getParameter("deviceip");
        try {
            String strSQL = "delete from tab_device_info where device_ip='"
                    + deviceip + "'";
            DataSetBean.executeUpdate(strSQL);
            strSQL = "select task_id from tab_ping_task_map where device_ip='"
                    + deviceip + "'";
            PrepareSQL psql1 = new PrepareSQL(strSQL);
            Cursor cursor = DataSetBean.getCursor(psql1.getSQL());
            Map fields = cursor.getNext();
            
            if (fields != null) {
                strSQL = "delete from tab_ping_packet_conf where task_id="
                        + fields.get("task_id");
                PrepareSQL psql2 = new PrepareSQL(strSQL);
                DataSetBean.executeUpdate(psql2.getSQL());
            }
            strSQL = "delete from tab_ping_task_map where device_ip='"
                    + deviceip + "'";
            PrepareSQL psql3 = new PrepareSQL(strSQL);
            DataSetBean.executeUpdate(psql3.getSQL());
            strMsg = "1";
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        
        return strMsg;
    }
    
    /**
     * 端到端业务ping包配置列表
     *
     *
     * @return
     */
    public Cursor PingPackage() {
        PrepareSQL pSQL;
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        gather_id = objUserRes.getUserProcesses();
        Cursor cursor = new Cursor();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            PingPackagesql = "select ping_packet_size, delay_threshold, timeout,  ping_packet_id from tab_ping_packet_conf where gather_id in(?)";
        }
        pSQL = new PrepareSQL(PingPackagesql);
        pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
        cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 端到端业务ping任务配置表 type 1为一般用户，2为VPN用户，0为俩种用户全满足
     *
     * @return cursor
     */
    public Cursor PingTast(int type) {
        PrepareSQL pSQL;
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        gather_id = objUserRes.getUserProcesses();
        
        Cursor cursor = new Cursor();
        if (type == 0) {
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                PingTastsql2 = "select task_id,user_type,sendping_ip,response_ip,warning_severity,task_desc,gather_id from tab_ping_task_map where gather_id in(?)";
            }
            pSQL = new PrepareSQL(PingTastsql2);
            pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
        } else {
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                PingTastsql = "select task_id,user_type,sendping_ip,response_ip,warning_severity,task_desc,gather_id from tab_ping_task_map where gather_id in(?) and user_type=?";
            }
            pSQL = new PrepareSQL(PingTastsql);
            pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
            pSQL.setInt(2, type);
        }
        cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 添加预定义ping报文
     *
     *
     * @return strMsg
     */
    public String PingPackageAdd() {
        String packagesize = request.getParameter("packagesize");
        String timelimit = request.getParameter("timelimit");
        String overtime = request.getParameter("overtime");
        String gather_id= request.getParameter("gather_id");
        try {
            String sql = "select max(ping_packet_id) as id from tab_ping_packet_conf";
            PrepareSQL psql = new PrepareSQL(sql);
            Cursor cursor = DataSetBean.getCursor(psql.getSQL());
            Map fields = cursor.getNext();
            if (fields != null) {
                long ping_packet_id = 0;
                if (((String) fields.get("id")).equals(""))
                    ping_packet_id = 1;
                else
                    ping_packet_id = Long.parseLong((String) fields.get("id")) + 1;
                sql = "insert into tab_ping_packet_conf values("
                        + ping_packet_id + "," + packagesize + "," + timelimit
                        + "," + overtime + ",'" + gather_id + "')";
//                logger.debug(sql);
                PrepareSQL psql2 = new PrepareSQL(sql);
                int row = DataSetBean.executeUpdate(psql2.getSQL());
                if (row > 0)
                    strMsg = String.valueOf(ping_packet_id);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        
        return strMsg;
    }
    
    /**
     * 修改预定义ping报文
     *
     *
     * @return strMsg
     */
    public String PingPackageModify() {
        String pingpackageid = request.getParameter("pingpackageid");
        String packagesize = request.getParameter("packagesize");
        String timelimit = request.getParameter("timelimit");
        String overtime = request.getParameter("overtime");
        // String gather_id = request.getParameter("gather_id");
        
        try {
            String sql = "update tab_ping_packet_conf set ping_packet_size="
                    + packagesize + "," + "delay_threshold=" + timelimit
                    + ",timeout=" + overtime + " where ping_packet_id="
                    + pingpackageid;
            PrepareSQL psql = new PrepareSQL(sql);
            int row = DataSetBean.executeUpdate(psql.getSQL());
            if (row > 0)
                strMsg = "1";
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 删除预定义ping报文
     *
     *
     * @return strMsg
     */
    public String PingPackageDel() {
        String pingpackageid = request.getParameter("pingpackageid");
        try {
            String sql = "delete from tab_ping_packet_conf where ping_packet_id="
                    + pingpackageid;
            PrepareSQL psql = new PrepareSQL(sql);
            int row = DataSetBean.executeUpdate(psql.getSQL());
            
            if (row > 0)
                strMsg = "1";
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }
    
    /**
     * 获取ping包信息
     *
     *
     * @return Cursor
     */
    public Cursor PingTastInfo() {
        PrepareSQL pSQL;
        HttpSession session = request.getSession();
        objUserRes = (DbUserRes) session.getAttribute("curUser");
        gather_id = objUserRes.getUserProcesses();
        Cursor cursor = new Cursor();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            PingPackagesql = "select ping_packet_size, delay_threshold, timeout,  ping_packet_id from tab_ping_packet_conf where gather_id in(?)";
        }
        pSQL = new PrepareSQL(PingPackagesql);
        pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
        cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 添加ping任务处理
     *
     *
     * @return String[]
     */
    public String[] PingTastAdd() {
        String sendip = request.getParameter("sendip");
        String acceptip = request.getParameter("acceptip");
        String alarmlevel = request.getParameter("alarmlevel");
        String taskdesc = request.getParameter("taskdesc");
        String usertype = request.getParameter("usertype");
        String uniformid = request.getParameter("uniformid");
        String subname = request.getParameter("subname");
        String gather_id = request.getParameter("gather_id");
        String[] str = new String[2];
        Cursor cursor = null;
        Map fields = null;
        
        if (usertype.compareTo("1") == 0) {
            try {
                String sql = "select * from tab_ping_task_map where sendping_ip='"
                        + sendip + "' and response_ip='" + acceptip + "'";
                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    sql = "select task_id,user_type,uniformid,subname,warning_severity,task_desc,gather_id from tab_ping_task_map where sendping_ip='"
                            + sendip + "' and response_ip='" + acceptip + "'";
                }
                PrepareSQL psql1 = new PrepareSQL(sql);
                cursor = DataSetBean.getCursor(psql1.getSQL());
                fields = cursor.getNext();
                if (fields == null) {
                    sql = "select max(task_id) as id from tab_ping_task_map";
                    PrepareSQL psql2 = new PrepareSQL(sql);
                    cursor = DataSetBean.getCursor(psql2.getSQL());
                    fields = cursor.getNext();
                    if (fields != null) {
                        long task_id = 0;
                        if (((String) fields.get("id")).equals(""))
                            task_id = 1;
                        else
                            task_id = Long.parseLong((String) fields.get("id")) + 1;
                        sql = "insert into tab_ping_task_map(task_id,user_type,sendping_ip,response_ip,warning_severity,task_desc,gather_id)  values("
                                + task_id
                                + ",1,'"
                                + sendip
                                + "','"
                                + acceptip
                                + "',"
                                + alarmlevel
                                + ",'"
                                + taskdesc
                                + "','"
                                + gather_id + "')";
                        PrepareSQL psql3 = new PrepareSQL(sql);
                        int row = DataSetBean.executeUpdate(psql3.getSQL());
                        if (row > 0)
                            // msg = String.valueOf(task_id);
                            str[0] = String.valueOf(task_id);
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        } else {
            try {
                String[] sub = subname.split(",");
                int j = 0;
                for (int i = 0; i < sub.length; i++) {
                    String sql = "select * from tab_ping_task_map where uniformid='"
                            + uniformid + "' and subname='" + sub[i] + "'";
                    // teledb
                    if (DBUtil.GetDB() == Global.DB_MYSQL) {
                        sql = "select task_id,user_type,uniformid,subname,warning_severity,task_desc,gather_id from tab_ping_task_map where uniformid='"
                                + uniformid + "' and subname='" + sub[i] + "'";
                    }
                    PrepareSQL psql4 = new PrepareSQL(sql);
                    cursor = DataSetBean.getCursor(psql4.getSQL());
                    fields = cursor.getNext();
                    if (fields == null) {
                        sql = "select max(task_id) as id from tab_ping_task_map";
                        PrepareSQL psql5 = new PrepareSQL(sql);
                        cursor = DataSetBean.getCursor(psql5.getSQL());
                        fields = cursor.getNext();
                        if (fields != null) {
                            long task_id = 0;
                            if (((String) fields.get("id")).equals(""))
                                task_id = 1;
                            else
                                task_id = Long.parseLong((String) fields
                                        .get("id")) + 1;
                            
                            sql = "insert into tab_ping_task_map(task_id,user_type,uniformid,subname,warning_severity,task_desc,gather_id)  values("
                                    + task_id
                                    + ",2,'"
                                    + uniformid
                                    + "','"
                                    + sub[i]
                                    + "',"
                                    + alarmlevel
                                    + ",'"
                                    + taskdesc + "','" + gather_id + "')";
                            PrepareSQL psql6 = new PrepareSQL(sql);
                            int row = DataSetBean.executeUpdate(psql6.getSQL());
                            if (row > 0) {
                                if (j == 0) {
                                    // msg = String.valueOf(task_id);
                                    // subuser = sub[i];
                                    str[0] = String.valueOf(task_id);
                                    str[1] = sub[i];
                                    
                                } else {
                                    // msg=msg+","+ String.valueOf(task_id);
                                    // subuser=subuser+","+sub[i];
                                    str[0] = str[0] + ","
                                            + String.valueOf(task_id);
                                    str[1] = str[1] + "," + sub[i];
                                }
                                j++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return str;
    }
    
    /**
     * 修改ping任务处理
     *
     *
     * @return strMsg
     */
    public String PingTastModify() {
        String tasktype = request.getParameter("tasktype");
        Cursor cursor = null;
        Map fields = null;
        if ("1".equals(tasktype)) {
            String taskid = request.getParameter("taskid");
            String sendip = request.getParameter("sendip");
            String acceptip = request.getParameter("acceptip");
            String alarmlevel = request.getParameter("alarmlevel");
            String taskdesc = request.getParameter("taskdesc");
            String gather_id = request.getParameter("gather_id");
            try {
                String sql = "select * from tab_ping_task_map where sendping_ip='"
                        + sendip
                        + "' and response_ip='"
                        + acceptip
                        + "' and task_id<>" + taskid;
                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    sql = "select task_id,user_type,sendping_ip,response_ip,warning_severity,task_desc,gather_id from tab_ping_task_map where sendping_ip='"
                            + sendip
                            + "' and response_ip='"
                            + acceptip
                            + "' and task_id<>" + taskid;
                }
                PrepareSQL psql1 = new PrepareSQL(sql);
                cursor = DataSetBean.getCursor(psql1.getSQL());
                fields = cursor.getNext();
                if (fields == null) {
                    sql = "update tab_ping_task_map set sendping_ip='" + sendip
                            + "',response_ip='" + acceptip
                            + "',warning_severity=" + alarmlevel
                            + ",task_desc='" + taskdesc + "',gather_id='"
                            + gather_id + "' where task_id=" + taskid;
                     logger.debug(sql);
                     PrepareSQL psql2 = new PrepareSQL(sql);
                    int row = DataSetBean.executeUpdate(psql2.getSQL());
                    if (row > 0)
                        strMsg = "1";
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        } else {
            String taskid = request.getParameter("taskid");
            String uniformid = request.getParameter("uniformid");
            String subname = request.getParameter("subname");
            String alarmlevel = request.getParameter("alarmlevel");
            String taskdesc = request.getParameter("taskdesc");
            String gather_id = request.getParameter("gather_id");
            
            try {
                String sql = "select * from tab_ping_task_map where uniformid='"
                        + uniformid
                        + "' and subname='"
                        + subname
                        + "' and task_id<>" + taskid;
                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    sql = "select task_id,user_type,uniformid,subname,warning_severity,task_desc,gather_id from tab_ping_task_map where uniformid='"
                            + uniformid
                            + "' and subname='"
                            + subname
                            + "' and task_id<>" + taskid;
                }
                PrepareSQL psql3 = new PrepareSQL(sql);
                cursor = DataSetBean.getCursor(psql3.getSQL());
                fields = cursor.getNext();
                if (fields == null) {
                    sql = "update tab_ping_task_map set uniformid='"
                            + uniformid + "',subname='" + subname
                            + "',warning_severity=" + alarmlevel
                            + ",task_desc='" + taskdesc + "',gather_id='"
                            + gather_id + "' where task_id=" + taskid;
                    PrepareSQL psql4 = new PrepareSQL(sql);
                    int row = DataSetBean.executeUpdate(psql4.getSQL());
                    if (row > 0)
                        strMsg = "1";
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return strMsg;
    }
    
    /**
     * 删除ping任务处理
     *
     *
     * @return strMsg
     */
    public String PingTastDel() {
        String taskid = request.getParameter("taskid");
        
        try {
            
            String sql = "delete from tab_ping_task_map where task_id="
                    + taskid;
            PrepareSQL psql1 = new PrepareSQL(sql);
            DataSetBean.executeUpdate(psql1.getSQL());
            sql = "delete from tab_ping_packet_task_map where task_id="
                    + taskid;
            PrepareSQL psql2 = new PrepareSQL(sql);
            DataSetBean.executeUpdate(psql2.getSQL());
            strMsg = "1";
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return strMsg;
    }    
    /**
     * 查询被考核业务唯一标识,字段task_type为1，亚信业务；2，DNS业务；3，MAIL-SMTP业务；4，MAIL-POP3业务；5，RADIUS业务
     * type为1,Mial业务；2,DNS业务；3,Redius业务
     *
     * @return Cursor
     */
    public Cursor getTaskName(int type) {
        PrepareSQL pSQL = new PrepareSQL(ServerSQL);
        switch (type) {
            case 1:
                pSQL.setInt(1, 3);
                pSQL.setInt(2, 4);
                break;
            case 2:
                pSQL.setInt(1, 2);
                pSQL.setInt(2, 2);
                break;
            case 3:
                pSQL.setInt(1, 5);
                pSQL.setInt(2, 5);
                break;
        }
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }
    
    /**
     * 服务利用率查询 服务是否可用 1为可用，0为不可用
     *
     * @return Cursor
     */
    public Cursor[] getServeRatioSearchList() {
        int k = 0;
        String[] sql = new String[2];
        String start = request.getParameter("start") + " "
                + request.getParameter("start_hms");
        String end = request.getParameter("end") + " "
                + request.getParameter("end_hms");
        String str_start = request.getParameter("hidstart");
        String str_end = request.getParameter("hidend");
        String[] arr_tasklist = request.getParameterValues("tasklist");
        String str_tasks = null;
        for (int i = 0; i < arr_tasklist.length; i++) {
            if (str_tasks == null) {
                str_tasks = arr_tasklist[i];
            } else {
                str_tasks += "','" + arr_tasklist[i];
            }
        }
        
        String taskSQL;
        if (str_tasks.equals("0")) {
            taskSQL = "";
        } else {
            taskSQL = " and task_name in('" + str_tasks + "')";
        }
        
        long lms = Long.parseLong(str_start) * 1000;
        Date dt = new Date(lms);
        
        int year_start = dt.getYear() + 1900;
        int month_start = dt.getMonth() + 1;
        
        lms = Long.parseLong(str_end) * 1000;
        dt = new Date(lms);
        
        int year_end = dt.getYear() + 1900;
        int month_end = dt.getMonth() + 1;
        
        String strSQL = null;
        String strTmpSQL = null;
        String tablename;
        if (year_start == year_end) {
            for (int i = month_start; i <= month_end; i++) {
                tablename = "tab_servicedata_" + year_start + "_" + i;
                if (strSQL == null) {
                    strSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + "  "
                            + taskSQL
                            + "  group by task_name";
                    strTmpSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1  "
                            + taskSQL
                            + " group by task_name";
                } else {
                    strSQL += " union all select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " "
                            + taskSQL
                            + "  group by task_name";
                    strTmpSQL += " union all select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1  "
                            + taskSQL
                            + " group by task_name";
                }
            }
        } else {
            for (int i = month_start; i <= 12; i++) {
                tablename = "tab_servicedata_" + year_start + "_" + i;
                if (strSQL == null) {
                    strSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + "  "
                            + taskSQL
                            + " group by task_name";
                    strTmpSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where   gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1 "
                            + taskSQL
                            + " group by task_name";
                } else {
                    strSQL += " union all select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " "
                            + taskSQL
                            + " group by task_name";
                    strTmpSQL += " union all select task_id,sum(time_interval) as total from "
                            + tablename
                            + " where gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1 "
                            + taskSQL
                            + " group by task_name";
                }
            }
            
            for (int i = 1; i <= month_end; i++) {
                tablename = "tab_servicedata_" + year_start + "_" + i;
                if (strSQL == null) {
                    strSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + "  "
                            + taskSQL
                            + " group by task_name";
                    strTmpSQL = "select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where   gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1 "
                            + taskSQL
                            + " group by task_name";
                } else {
                    strSQL += " union all select task_name,sum(time_interval) as total from "
                            + tablename
                            + " where  gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " "
                            + taskSQL
                            + " group by task_name";
                    strTmpSQL += " union all select task_id,sum(time_interval) as total from "
                            + tablename
                            + " where gather_time>"
                            + str_start
                            + " and gather_time<="
                            + str_end
                            + " and usable_or_not=1 "
                            + taskSQL
                            + " group by task_name";
                }
            }
        }
        sql[0] = strSQL;
        sql[1] = strTmpSQL;
        int num = sql.length;
        Cursor[] cursor = new Cursor[num];
        for (k = 0; k < num; k++) {
        	PrepareSQL psql = new PrepareSQL(sql[k]);
            cursor[k] = DataSetBean.getCursor(psql.getSQL());
        }
        return cursor;
    }
    /**
     * 日Ppoe接通率
     *
     * @return Cursor
     */
    public Cursor getPpoeRate(long start,long end){
        String sql="select pppoe_success_ratio from tab_pppoe_statis_daily where statis_time>="+start+" and statis_time<="+end;
        PrepareSQL psql = new PrepareSQL(sql);
        Cursor cursor = DataSetBean.getCursor(psql.getSQL());
        return cursor;
    }
    /**
     * 日pppoe详细数据
     *
     * @return Cursor
     */
    public Cursor getPpoeData(long start,long end){
        String sql = "select * from tab_pppoe_data_orig where gather_time>=" + start + " and gather_time<=" + end + " order by device_ip, gather_time";
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            // 没找到该表，mysql先预留
            sql = "select * from tab_pppoe_data_orig where gather_time>=" + start + " and gather_time<=" + end + " order by device_ip, gather_time";
        }
        PrepareSQL psql = new PrepareSQL(sql);
        Cursor cursor = DataSetBean.getCursor(psql.getSQL());
        return cursor;
    }
    /**
     * 服务利用率月报表查询
     *
     * @return Cursor
     */
    public Cursor getReportQuery(long start,long end){
        String[] arr_tasklist = request.getParameterValues("tasklist");
        String str_tasks= null;
        String taskSQL;
        String ReportQuerySql;
        for(int i=0;i<arr_tasklist.length;i++){
            if(str_tasks == null){
                str_tasks = arr_tasklist[i];
            } else{
                str_tasks += "','"+ arr_tasklist[i];
            }
        }
        if("0".equals(str_tasks)){
            taskSQL = "";
        } else{
            taskSQL = " and task_name in('"+str_tasks+"')";
        }
        ReportQuerySql="select * from tab_daily_servicestat where statis_time>"+ start +" and statis_time<="+ end +" "+ taskSQL;
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            // 没找到该表，mysql先预留
            ReportQuerySql="select * from tab_daily_servicestat where statis_time>"+ start +" and statis_time<="+ end +" "+ taskSQL;
        }
        PrepareSQL psql = new PrepareSQL(ReportQuerySql);
        Cursor cursor = DataSetBean.getCursor(psql.getSQL());
        logger.debug("服务利用率月报表查询SQL:"+ReportQuerySql);
        return cursor;
    }
    
}
