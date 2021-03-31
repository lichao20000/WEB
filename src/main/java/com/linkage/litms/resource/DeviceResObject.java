package com.linkage.litms.resource;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.ServerCode;
import com.linkage.litms.system.User;

public class DeviceResObject {
    private User USER = null;
    private String splitCharator = ",";
    private String device_id = null;
    private String device_name = null;
    private String oui = null;
    private String device_serialnumber = null;
    private String devicetype_id = "";
    private String city_id = null;
    private String gather_id = "szx";
    private String cpe_username = "itms";
    private String cpe_passwd = "itms";
    private String acs_username = "hgw";
    private String acs_passwd = "hgw";
//    private String device_status = "1";
//    private String maxEnvelopes = "1";
//    private String retryCount = "0";
    private String port = "80";
    private long cpe_currentupdatetime = 0l;
//    private String device_id_ex = "";
    private String staff_id = "admin";
    private long complete_time = 0l;
    private long buy_time = 0l;
    //当前电信维护密码
    private String x_com_passwd = "";
    //首次电信维护密码
    private String x_com_passwd_old = "";
    /**
     * 设备资源添加sql
     */
    private String m_DeviceAdd_SQL = "insert into tab_gw_device (device_id,device_name,oui," +
            "device_serialnumber,devicetype_id,city_id,complete_time,buy_time,staff_id,gather_id," +
            "device_status,maxenvelopes,retrycount,cr_port,cpe_currentupdatetime,acs_username,"  +
            "acs_passwd,cpe_username,cpe_passwd,manage_staff,gw_type,interface_id,x_com_passwd," +
            "x_com_passwd_old,dev_sub_sn)" +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private PrepareSQL pSQL = null;
    public DeviceResObject(User user){
        pSQL = new PrepareSQL();
        USER = user;
    }
    public DeviceResObject(){
        pSQL = new PrepareSQL();
    }
    public boolean setDataSource(String str){
        if(str == null || str.length() == 0)
            return false;
        String[] arr = str.split(splitCharator);
        if(arr.length >= 4){
            device_name         = arr[0];
            oui                 = arr[1];
            device_serialnumber = arr[2];
            devicetype_id       = arr[3];
            
            if(LipossGlobals.isXJDX()){
                x_com_passwd_old = arr[4];
            }else{
            	x_com_passwd_old = ServerCode.TelecomPasswd;
            }
            x_com_passwd = x_com_passwd_old;
            
            if(USER != null){
              city_id           = USER.getCityId();
              staff_id     = USER.getAccount();
            }
            return true;
        }
        return false;
    }
    public void setDeviceId(int id){
        this.device_id = String.valueOf(id);
    }
    
    public void setUpdate_Buy_Completetime(long time) {
        complete_time = buy_time = this.cpe_currentupdatetime = time;
    }
    public String getInsertSQL(String gw_type){
        pSQL.setSQL(m_DeviceAdd_SQL);
        pSQL.setString(1,device_id);
        pSQL.setString(2,device_name);
        pSQL.setString(3,oui);
        pSQL.setString(4,device_serialnumber);
        pSQL.setStringExt(5,devicetype_id,false);
        pSQL.setString(6,city_id);
        pSQL.setLong(7,complete_time);
        pSQL.setLong(8,buy_time);
        pSQL.setString(9,staff_id);
        pSQL.setString(10,gather_id);
        pSQL.setInt(11,1);//device_status
        pSQL.setInt(12,1);//maxEnvelopes
        pSQL.setInt(13,0);//retrycount
        pSQL.setStringExt(14,port,false);
        pSQL.setLong(15,cpe_currentupdatetime);
        pSQL.setString(16,acs_username);
        pSQL.setString(17,acs_passwd);
        pSQL.setString(18,cpe_username);
        pSQL.setString(19,cpe_passwd);
        pSQL.setString(20,staff_id);
        pSQL.setInt(21,Integer.parseInt(gw_type));
        //网关来源，2表示批量导入
        pSQL.setInt(22, 2);
        pSQL.setString(23, x_com_passwd);
        pSQL.setString(24, x_com_passwd_old);
        pSQL.setString(25, device_serialnumber.substring(device_serialnumber.length()-6,device_serialnumber.length()));
        String sql = pSQL.getSQL();
        return sql;
    }
    
    //add by zhaixf
	public String getOui() {
		return oui;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getDevice_serialnumber() {
		return device_serialnumber;
	}
	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}
    
}
