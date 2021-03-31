package com.linkage.litms.netcutover;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-6-14
 * @category com.linkage.litms.netcutover
 * 版权：南京联创科技 网管科技部
 *
 */
public class BnetSheetOperator {

    private final static String checkEgwCustomer = "select user_id from tab_egwcustomer where serv_type_id=? and e_id=? and user_state in('1','2')";
    private final static String checkCustomer    = "select id from tab_customerinfo where customer_id=? and customer_state in (1,2)";
    private final static String insertCustomerInfo = "insert into tab_customerinfo(id,customer_id,customer_name,customer_state,update_time,oui,device_serialnumber,city_id,office_id) values(?,?,?,?,?,?,?,?,?)";
    private final static String insertEgwCustomer = "insert into tab_egwcustomer(user_id,username,passwd,oui,device_serialnumber,serv_type_id,e_id) values(?,?,?,?,?,?,?)";
    private final static String insertBnetSheet   = "insert into tab_egw_bsn_open_original(id,bnet_id,bnet_account,product_spec_id,type,status,wp_flag,device_type,oui,device_serialnumber,time) values(?,?,?,?,?,?,?,?,?,?,?)";
    
    private PrepareSQL pSQL = null;
    
    public BnetSheetOperator(){
	pSQL = new PrepareSQL();
    }
    /**
     * bnet手工工单保存
     * @param request
     * @return
     */
    public boolean saveBnetSheet(HttpServletRequest request){
	String oui 	= request.getParameter("oui");
	String city_id  = request.getParameter("city_id");
	String office_id = request.getParameter("office_id");
	String customer_id = request.getParameter("customer_id");//       
	String customer_name = request.getParameter("customer_name");
	String device_serialnumber = request.getParameter("device_serialnumber");
	String device_type = request.getParameter("device_type");
	String some_service = request.getParameter("some_service");//业务类型
	String oper_type = request.getParameter("oper_type");//操作类型
	String service_account =  request.getParameter("service_account");
	String service_pwd = request.getParameter("service_pwd");
	
	return insertCustomerInfo(customer_id,customer_name,oui,device_serialnumber,city_id,office_id)
		&& insertEgwCustomer(service_account,service_pwd,oui,device_serialnumber,some_service,customer_id)
		&& insertBnetSheet(customer_id, customer_name, some_service, device_type,oper_type,oui,device_serialnumber);
    }
    /**
     * 入客户表
     * @param customer_id
     * @param customer_name
     * @param oui
     * @param device_serialnumber
     * @param city_id
     * @param office_id
     * @return
     */
    private boolean insertCustomerInfo(String customer_id,String customer_name,String oui,String device_serialnumber,String city_id,String office_id){
	//存在则直接返回，不插入数据库
	if(checkCustomerInfo(customer_id))
	    return true;
	String id = java.util.UUID.randomUUID().toString();
	//"insert into tab_customerinfo(id,customer_id,customer_name,customer_state,update_time,oui,device_serialnumber,city_id,office_id) values(?,?,?,?,?,?,?,?,?)";
	pSQL.setSQL(insertCustomerInfo);
	pSQL.setString(1, id);
	pSQL.setString(2, customer_id);
	pSQL.setString(3, customer_name);
	pSQL.setInt(4, 1);
	pSQL.setLong(5, System.currentTimeMillis() / 1000);
	pSQL.setString(6, oui);
	pSQL.setString(7, device_serialnumber);
	pSQL.setString(8, city_id);
	pSQL.setString(9, office_id);
	return DataSetBean.executeUpdate(pSQL.getSQL()) > 0;
    }
    /**
     * 入库之前需要检查数据库中是否存在记录
     * @param customer_id
     * @return 存在则返回true
     */
    private boolean checkCustomerInfo(String customer_id){
	pSQL.setSQL(checkCustomer);
	pSQL.setString(1, customer_id);
	Object record = DataSetBean.getRecord(pSQL.getSQL());
	return record != null;
    }
    /**
     * 入用户表
     * @param username
     * @param passwd
     * @param oui
     * @param device_serialnumber
     * @param serv_type_id
     * @param e_id
     * @return
     */
    private boolean insertEgwCustomer(String username,String passwd,String oui,String device_serialnumber,String serv_type_id,String e_id){
	//存在则直接返回，不插入数据库
	if(checkEgwCustomer(serv_type_id,e_id)){
	    return true;
	}
	long user_id =  DataSetBean.getMaxId("tab_egwcustomer", "user_id");
	//"insert into tab_egwcustomer(user_id,username,passwd,oui,device_serialnumber,serv_type_id,e_id) values(?,?,?,?,?,?,?)";
	pSQL.setSQL(insertEgwCustomer);
	pSQL.setLong(1, user_id);
	pSQL.setString(2, username);
	pSQL.setString(3, passwd);
	pSQL.setString(4, oui);
	pSQL.setString(5, device_serialnumber);
	pSQL.setStringExt(6, serv_type_id,false);
	pSQL.setString(7, e_id);
	
	return DataSetBean.executeUpdate(pSQL.getSQL()) > 0;
    }
    
    private boolean checkEgwCustomer(String serv_type_id,String e_id){
	pSQL.setSQL(checkEgwCustomer);
	pSQL.setStringExt(1, serv_type_id,false);
	pSQL.setString(2, e_id);
	Object record = DataSetBean.getRecord(pSQL.getSQL());
	return record != null;
    }
    
    /**
     * 入工单表
     * @param bnet_id
     * @param bnet_account
     * @param product_spec_id 业务类型
     * @param device_type
     * @param type 操作类型
     * @return
     */
    private boolean insertBnetSheet(String bnet_id,String bnet_account,String product_spec_id,String device_type,String type,String oui,String device_serialnumber){
	String id = String.valueOf(System.currentTimeMillis());
	pSQL.setSQL(insertBnetSheet);
	//insert into tab_egw_bsn_open_original(id,bnet_id,bnet_account,product_spec_id,type,status,wp_flag,device_type,oui,device_serialnumber,time)
	pSQL.setString(1, id);
	pSQL.setString(2, bnet_id);
	pSQL.setString(3, bnet_account);
	pSQL.setString(4, product_spec_id);
	pSQL.setStringExt(5, type,false);
	pSQL.setInt(6, 0);
	pSQL.setInt(7, 2);
	pSQL.setString(8, device_type);
	pSQL.setString(9, oui);
	pSQL.setString(10, device_serialnumber);
	pSQL.setLong(11, System.currentTimeMillis() / 1000);
	return DataSetBean.executeUpdate(pSQL.getSQL()) > 0;
    }
}

